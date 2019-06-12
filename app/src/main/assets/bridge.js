(function (root, factory) {
  root.JsBridge = factory(root);
}(typeof window === "undefined" ? this : window, function (win) {
  if (!win.document) {
    return {};
  }

  var doc = win.document,
    title = doc.title,
    ua = navigator.userAgent.toLowerCase(),
    platform = navigator.platform.toLowerCase(),
    isMacorWin = !(!platform.match("mac") && !platform.match("win")),
    isandroid = -1 !== ua.indexOf("android"),
    isphoneorpad = -1 !== ua.indexOf("iphone") || -1 !== ua.indexOf("ipad"),
    JsBridge = {
      usable: false,
      init: function (bridge) {
        /*注册提供给native的接口*/
        bridge.registerHandler("exam", function (message, responseCallback) {

          var result = document.getElementById("result");
          result.innerHTML = 'native传递的数据:' + JSON.stringify(message);
          responseCallback({
            success: false,
            msg: "ok",
            values: {
              msg: "js回调native"
            }
          });
        });

        bridge.registerHandler("exam1", function (message, responseCallback) {

          var result = document.getElementById("result");
          result.innerHTML = 'native传递的数据:' + JSON.stringify(message);
          responseCallback({
            status: "1",
            msg: "ok",
            values: {
              cityName: message.cityName,
              cityProvince: message.cityProvince
            }
          });
        });

        bridge.registerHandler("exam2", function (message, responseCallback) {

          var result = document.getElementById("result");
          result.innerHTML = 'native传递的数据:' + JSON.stringify(message);
          responseCallback({
            status: "1",
            msg: "ok",
            values: {
              city: {
                cityName: message.cityName,
                cityProvince: message.cityProvince
              }

            }
          });
        });

        bridge.registerHandler("exam3", function (message, responseCallback) {

          var result = document.getElementById("result");
          result.innerHTML = 'native传递的数据:' + JSON.stringify(message);
          responseCallback({
            status: "1",
            msg: "ok",
            values: {
              cityName: '北京',
              cityProvince: '北京'

            }
          });
        });

        bridge.registerHandler("exam4", function (message, responseCallback) {

          var result = document.getElementById("result");
          result.innerHTML = 'native传递的数据:' + JSON.stringify(message);
          responseCallback({
            status: "1",
            msg: "ok",
            values: {}
          });
        });

        return this;
      }
    };

  if (window._JSNativeBridge) {
    console.log("native injection js success!");
    window._JSNativeBridge.protocol.scheme = 'icourt';
    window._JSNativeBridge.protocol.host = 'alpha';
    window._JSNativeBridge.debug = true;
    JsBridge.init(window._JSNativeBridge);
  } else {
    console.log("native injection js wrong!");
    document.addEventListener(
      'JsBridgeInit',
      function (event) {
        console.log('------------------JS_Bridge------------------');
        window._JSNativeBridge.protocol.scheme = 'icourt';
        window._JSNativeBridge.protocol.host = 'alpha';
        event.bridge.debug = true;
        JsBridge.init(event.bridge);
      },
      false
    );
  }

  return JsBridge;
}));


window.icourt = {

  alpha: {
    test: window._JSNativeBridge.createApiFunc('test'),
    test1: window._JSNativeBridge.createApiFunc('test1'),
    test2: window._JSNativeBridge.createApiFunc('test2'),
    test3: window._JSNativeBridge.createApiFunc('test3'),
    test4: window._JSNativeBridge.createApiFunc('test4'),
    preProjectDetail: window._JSNativeBridge.createApiFunc('preProjectDetail'),
    addAttachment: window._JSNativeBridge.createApiFunc('addAttachment'),
    previewAttachment: window._JSNativeBridge.createApiFunc('previewAttachment'),
    openContactDetail: window._JSNativeBridge.createApiFunc('openContactDetail')
  }
}
