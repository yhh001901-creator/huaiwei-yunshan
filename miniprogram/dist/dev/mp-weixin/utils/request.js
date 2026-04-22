"use strict";
const BASE_URL = "http://127.0.0.1:8083";
const imageBaseURL = "http://127.0.0.1:8083";
function request(options) {
  var url = BASE_URL + options.url;
  var method = options.method || "GET";
  var data = options.data || {};
  var token = wx.getStorageSync("token");
  var header = { "Content-Type": "application/json" };
  if (token) { header["Authorization"] = "Bearer " + token; }
  Object.assign(header, options.header || {});
  console.log("[Request] " + method + " " + url);
  return new Promise(function(resolve, reject) {
    wx.request({
      url: url,
      method: method,
      data: data,
      header: header,
      timeout: 15000,
      success: function(res) {
        console.log("[Response] " + res.statusCode, res.data);
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data.data);
          } else {
            wx.showToast({ title: res.data.msg || res.data.message || "请求失败", icon: "none" });
            reject(new Error(res.data.msg || res.data.message));
          }
        } else if (res.statusCode === 401) {
          wx.removeStorageSync("token");
          wx.removeStorageSync("userInfo");
          wx.redirectTo({ url: "/pages/login/login" });
          reject(new Error("未登录"));
        } else {
          reject(new Error("HTTP " + res.statusCode));
        }
      },
      fail: function(err) {
        console.error("[Request Fail]", err);
        wx.showToast({ title: err.errMsg || "网络异常", icon: "none" });
        reject(err);
      }
    });
  });
}
function get(url, data, header) { return request({ url: url, method: "GET", data: data, header: header }); }
function post(url, data, header) { return request({ url: url, method: "POST", data: data, header: header }); }
exports.get = get;
exports.imageBaseURL = imageBaseURL;
exports.post = post;
exports.request = request;
