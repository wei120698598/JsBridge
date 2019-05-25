(function () {

  if (window._JSNativeBridge) {
    return;
  }

  const messageHandlers = {};

  const responseCallbacks = {};
  let callbacks = {};
  let uniqueId = 1;

  function registerHandler (handlerName, handler) {
    messageHandlers[handlerName] = handler;
  }

  function createApiFunc (apiName, params) {
    return function (params) {
      return new Promise(function (resolve, reject) {
        let nextId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();
        callbacks[nextId] = {
          resolve: function (result) {
            delete callbacks[nextId]
            if (typeof result === 'string') {
              try {
                result = decodeURIComponent(result.replace(/\+/g, '%20'))
                let resultObj = JSON.parse(result)
                resolve(resultObj)
                return
              } catch (e) {
              }
            }
            resolve(result)
          },
          reject: function (error) {
            delete callbacks[nextId];
            console.log(error);
            if (typeof error === 'string') {
              try {
                error = decodeURIComponent(error.replace(/\+/g, '%20'));
                let errorObj = JSON.parse(error);
                reject(errorObj);
                return
              } catch (e) {
              }
            }
            reject(error)
          }
        }

        var request = {};
        request[_JSNativeBridge.request.interfaceName] = apiName;
        request[_JSNativeBridge.request.valuesName] = params;
        request[_JSNativeBridge.request.callbackIdName] = nextId;
        _doSend(request);
      })
    }
  }

  /*向native发送请求信息*/
  function _doSendRequest (handlerName, params, responseCallback) {

    var callbackId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();
    responseCallbacks[callbackId] = responseCallback;

    var request = {};
    request[_JSNativeBridge.request.interfaceName] = handlerName;
    request[_JSNativeBridge.request.valuesName] = params;
    request[_JSNativeBridge.request.callbackIdName] = callbackId;
    _doSend(request);
  }

  /*向native发送response（响应信息）*/
  function _doSendResponse (responseId, responseData) {
    var response = {};
    response[_JSNativeBridge.response.responseIdName] = responseId;
    response[_JSNativeBridge.response.responseName] = responseData;
    _doSend(response);
  }

  //sendMessage add message, 触发native处理 sendMessage
  function _doSend (message) {
    if (_isEmpty(_JSNativeBridge.protocol.scheme) || _isEmpty(_JSNativeBridge.protocol.host)) {
      throw "_JSNativeBridge.protocol.scheme 或 _JSNativeBridge.protocol.host不能为空";
    }
    prompt(_JSNativeBridge.protocol.scheme + '://' + _JSNativeBridge.protocol.host + '?' + JSON.stringify(message));
  }

  function _isEmpty (str) {
    if (str === null) {
      return true;
    }

    if (str.replace(/(^s*)|(s*$)/g, "").length === 0) {
      return true;
    }
  }

  function callbackPromise (type, callbackID, args) {
    console.log(args);
    const callback = callbacks[callbackID]
    callback[type](args)
  }

  function _doNativeResponse (response) {
    if (_JSNativeBridge.debug) {
      console.log(' response=' + JSON.stringify(response));
    }
    var responseIdValue = response[_JSNativeBridge.response.responseIdName];
    console.log(JSON.stringify(response[_JSNativeBridge.response.responseName]))
    if (responseIdValue) {
      callbackPromise(response[_JSNativeBridge.response.responseName].success ? 'resolve' : 'reject', responseIdValue, response[_JSNativeBridge.response.responseName].result)
      return true;
    }

    return false;
  }

  /*处理native的请求*/
  function _doNativeRequest (request) {
    var responseCallback;
    var callbackId = request[_JSNativeBridge.request.callbackIdName];
    /*native的request携带callback id*/
    if (callbackId) {
      responseCallback = function (responseData) {
        _doSendResponse(callbackId,
          responseData || {}
        );
      };
    }

    var handler = _JSNativeBridge._messageHandler;
    var handlerName = request[_JSNativeBridge.request.interfaceName];
    if (handlerName) {
      /*说明js存在这样的接口*/
      if (messageHandlers[handlerName]) {
        handler = messageHandlers[handlerName];
      } else {
        /*否则发送失败的信息*/
        _doSendResponse(callbackId,
          {
            status: "-1",
            msg: "Js can't find correspond method."
          }
        );
      }
    }
    try {
      /*发送*/
      handler(request[_JSNativeBridge.request.valuesName], responseCallback);
    } catch (exception) {
      if (typeof console !== 'undefined') {
        alert("_JSNativeBridge: WARNING: javascript handler threw.", message, exception);
      }
    }
  }

  /*分发从native传递的信息*/
  function _dispatchMessageFromNative (messageJSON) {
    setTimeout(function () {
      var message = JSON.parse(messageJSON);
      if (!_doNativeResponse(message)) {
        _doNativeRequest(message);
      }

    });
  }

  //暴漏给native的唯一一个通信方法
  function _handleMessageFromNative (messageJSON) {
    console.log(1)
    console.log(messageJSON)
    _dispatchMessageFromNative(messageJSON);
  }

  var _JSNativeBridge = window._JSNativeBridge = {
    createApiFunc: createApiFunc,
    callbackPromise: callbackPromise,
    registerHandler: registerHandler,
    _doSendRequest: _doSendRequest,
    _handleMessageFromNative: _handleMessageFromNative,
    request: {
      interfaceName: "handlerName",
      callbackIdName: "callbackId",
      valuesName: "params"
    },
    response: {
      responseIdName: "responseId",
      responseName: "data",
    },
    protocol: {
      scheme: "",
      host: "",
    },
    debug: true

  };

  var doc = document;
  var readyEvent = doc.createEvent('Events');
  readyEvent.initEvent('JsBridgeInit');
  readyEvent.bridge = _JSNativeBridge;
  doc.dispatchEvent(readyEvent);
  if (_JSNativeBridge.debug) {
    console.log("   -- initbridge end");
  }
})();
