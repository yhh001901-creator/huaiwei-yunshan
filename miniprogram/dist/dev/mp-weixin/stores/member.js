"use strict";
const common_vendor = require("../common/vendor.js");
const utils_request = require("../utils/request.js");

const state = common_vendor.reactive({
  memberInfo: null,
  pointList: [],
  couponTemplates: [],
  myCoupons: [],
  memberPrices: [],
  checkedIn: false
});

const isMember = common_vendor.computed(() => {
  var _a;
  return !!((_a = state.memberInfo) == null ? void 0 : _a.isMember);
});

const memberExpire = common_vendor.computed(() => {
  var _a;
  return ((_a = state.memberInfo) == null ? void 0 : _a.memberExpire) || "";
});

const point = common_vendor.computed(() => {
  var _a;
  return ((_a = state.memberInfo) == null ? void 0 : _a.point) || 0;
});

async function fetchMemberInfo() {
  try {
    const data = await utils_request.get("/api/member/info");
    state.memberInfo = data;
    return data;
  } catch (e) {
    console.error("获取会员信息失败:", e);
    return null;
  }
}

async function activateMember(userId) {
  try {
    await utils_request.post("/api/member/activate", { userId: userId });
    common_vendor.index.showToast({ title: "开通会员成功", icon: "success" });
    await fetchMemberInfo();
    return { success: true };
  } catch (e) {
    return { success: false, message: e.message || "开通失败" };
  }
}

async function checkIn() {
  try {
    const data = await utils_request.post("/api/member/checkin");
    common_vendor.index.showToast({ title: data.msg || "签到成功", icon: "success" });
    state.checkedIn = true;
    await fetchMemberInfo();
    return { success: true };
  } catch (e) {
    return { success: false, message: e.message || "签到失败" };
  }
}

async function fetchPointList() {
  try {
    const data = await utils_request.get("/api/member/point/list");
    state.pointList = data || [];
    return data;
  } catch (e) {
    console.error("获取积分流水失败:", e);
    return [];
  }
}

async function fetchCouponTemplates() {
  try {
    const data = await utils_request.get("/api/coupon/template/list");
    state.couponTemplates = data || [];
    return data;
  } catch (e) {
    console.error("获取优惠券模板失败:", e);
    return [];
  }
}

async function receiveCoupon(templateId) {
  try {
    await utils_request.post("/api/coupon/template/receive/" + templateId);
    common_vendor.index.showToast({ title: "领取成功", icon: "success" });
    return { success: true };
  } catch (e) {
    return { success: false, message: e.message || "领取失败" };
  }
}

async function fetchMyCoupons() {
  try {
    const data = await utils_request.get("/api/coupon/my/list");
    state.myCoupons = data || [];
    return data;
  } catch (e) {
    console.error("获取我的优惠券失败:", e);
    return [];
  }
}

async function fetchMemberPrices() {
  try {
    const data = await utils_request.get("/api/dishMemberPrice/list");
    state.memberPrices = data || [];
    return data;
  } catch (e) {
    console.error("获取会员价失败:", e);
    return [];
  }
}

function getMemberPrice(dishId) {
  var item = state.memberPrices.find(function(p) { return p.dishId === dishId; });
  return item ? item.memberPrice : null;
}

function useMemberStore() {
  return {
    state,
    isMember,
    memberExpire,
    point,
    fetchMemberInfo,
    activateMember,
    checkIn,
    fetchPointList,
    fetchCouponTemplates,
    receiveCoupon,
    fetchMyCoupons,
    fetchMemberPrices,
    getMemberPrice
  };
}
exports.useMemberStore = useMemberStore;
