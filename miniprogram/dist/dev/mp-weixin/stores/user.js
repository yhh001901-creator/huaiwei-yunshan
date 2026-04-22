"use strict";
const common_vendor = require("../common/vendor.js");
const utils_request = require("../utils/request.js");
const state = common_vendor.reactive({
  token: common_vendor.index.getStorageSync("token") || "",
  userInfo: (() => {
    try {
      var val = common_vendor.index.getStorageSync("userInfo");
      if (!val) return null;
      return typeof val === "string" ? JSON.parse(val) : val;
    } catch (e) {
      return null;
    }
  })()
});
const isLoggedIn = common_vendor.computed(() => !!state.token);
const username = common_vendor.computed(() => {
  var _a;
  return ((_a = state.userInfo) == null ? void 0 : _a.username) || "";
});
const roleName = common_vendor.computed(() => {
  var _a;
  const roleMap = { "1": "管理员", "2": "收银员", "3": "后厨", "4": "顾客" };
  return roleMap[(_a = state.userInfo) == null ? void 0 : _a.role] || "顾客";
});
function setToken(token) {
  state.token = token;
  common_vendor.index.setStorageSync("token", token);
}
function setUserInfo(userInfo) {
  state.userInfo = userInfo;
  common_vendor.index.setStorageSync("userInfo", userInfo);
}
function login(credentials) {
  return utils_request.request({
    url: "/api/auth/login",
    method: "POST",
    data: credentials
  }).then((res) => {
    if (res.token) {
      setToken(res.token);
      setUserInfo({
        userId: res.userId,
        username: res.username,
        phone: res.phone,
        role: res.role,
        roleName: res.roleName
      });
      return { success: true };
    }
    return { success: false, message: res.msg || res.message || "登录失败" };
  });
}
function register(credentials) {
  return utils_request.request({
    url: "/api/auth/register",
    method: "POST",
    data: credentials
  }).then((res) => {
    if (res.token) {
      setToken(res.token);
      setUserInfo({
        userId: res.userId,
        username: res.username,
        phone: res.phone,
        role: res.role,
        roleName: res.roleName
      });
    }
    return { success: true };
  }).catch((err) => {
    return { success: false, message: err.msg || err.message || "注册失败" };
  });
}
async function refreshUserInfo() {
  try {
    const data = await utils_request.get("/api/user/profile");
    setUserInfo(data);
    return data;
  } catch (e) {
    console.error("刷新用户信息失败:", e);
    return null;
  }
}
async function updateProfile(userData) {
  try {
    const data = await utils_request.request({
      url: "/api/user/profile",
      method: "PUT",
      data: userData
    });
    setUserInfo({ ...state.userInfo, ...data });
    return { success: true };
  } catch (e) {
    return { success: false, message: e.message || "更新失败" };
  }
}
async function updatePassword(oldPassword, newPassword) {
  try {
    await utils_request.request({
      url: "/api/user/password",
      method: "PUT",
      data: { oldPassword, newPassword }
    });
    return { success: true };
  } catch (e) {
    return { success: false, message: e.message || "修改密码失败" };
  }
}
function logout() {
  utils_request.post("/api/auth/logout").catch(() => {
  });
  state.token = "";
  state.userInfo = null;
  common_vendor.index.removeStorageSync("token");
  common_vendor.index.removeStorageSync("userInfo");
  common_vendor.index.removeStorageSync("cart_items");
  common_vendor.index.removeStorageSync("cart_table_no");
}
function useUserStore() {
  return {
    state,
    isLoggedIn,
    username,
    roleName,
    setToken,
    setUserInfo,
    login,
    register,
    logout,
    refreshUserInfo,
    updateProfile,
    updatePassword
  };
}
exports.useUserStore = useUserStore;
