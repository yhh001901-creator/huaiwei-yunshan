"use strict";
const common_vendor = require("../../common/vendor.js");
const stores_user = require("../../stores/user.js");
const stores_member = require("../../stores/member.js");
const _sfc_main = {
  __name: "member",
  setup(__props) {
    const userStore = stores_user.useUserStore();
    const memberStore = stores_member.useMemberStore();
    const activeTab = common_vendor.ref("coupons");
    const showPointList = common_vendor.ref(false);
    const checkedIn = common_vendor.ref(false);

    const memberInfo = common_vendor.computed(() => memberStore.state.memberInfo);
    const isMember = common_vendor.computed(() => memberStore.isMember);
    const memberExpire = common_vendor.computed(() => memberStore.memberExpire);
    const pointValue = common_vendor.computed(() => memberStore.point);
    const username = common_vendor.computed(() => {
      var _a;
      return ((_a = memberStore.state.memberInfo) == null ? void 0 : _a.nickname) || "未开通";
    });
    const phone = common_vendor.computed(() => {
      var _a;
      return ((_a = memberStore.state.memberInfo) == null ? void 0 : _a.phone) || "";
    });
    const pointList = common_vendor.computed(() => memberStore.state.pointList);
    const couponTemplates = common_vendor.computed(() => memberStore.state.couponTemplates);
    const myCoupons = common_vendor.computed(() => memberStore.state.myCoupons);

    async function loadData() {
      try {
        await memberStore.fetchMemberInfo();
        await memberStore.fetchMemberPrices();
        await memberStore.fetchCouponTemplates();
        await memberStore.fetchMyCoupons();
      } catch (e) {
        console.error("加载会员数据失败:", e);
      }
    }

    async function handleActivate() {
      var _a;
      if (!((_a = userStore.state.userInfo) == null ? void 0 : _a.userId)) {
        common_vendor.index.showToast({ title: "请先登录", icon: "none" });
        return;
      }
      common_vendor.index.showModal({
        title: "开通会员",
        content: "确认开通会员？开通后可享受会员价和更多专属权益",
        confirmColor: "#F97316",
        success: async (res) => {
          if (res.confirm) {
            common_vendor.index.showLoading({ title: "开通中..." });
            const result = await memberStore.activateMember(userStore.state.userInfo.userId);
            common_vendor.index.hideLoading();
            if (!result.success) {
              common_vendor.index.showToast({ title: result.message || "开通失败", icon: "none" });
            }
          }
        }
      });
    }

    async function handleCheckIn() {
      if (checkedIn.value) {
        common_vendor.index.showToast({ title: "今日已签到", icon: "none" });
        return;
      }
      const result = await memberStore.checkIn();
      if (!result.success) {
        common_vendor.index.showToast({ title: result.message || "签到失败", icon: "none" });
      }
    }

    async function handleReceiveCoupon(templateId) {
      if (!isMember.value) {
        common_vendor.index.showToast({ title: "请先开通会员", icon: "none" });
        return;
      }
      const result = await memberStore.receiveCoupon(templateId);
      if (result.success) {
        await memberStore.fetchMyCoupons();
      } else {
        common_vendor.index.showToast({ title: result.message || "领取失败", icon: "none" });
      }
    }

    function switchTab(tab) {
      activeTab.value = tab;
    }

    function togglePointList() {
      showPointList.value = !showPointList.value;
      if (showPointList.value && pointList.value.length === 0) {
        memberStore.fetchPointList();
      }
    }

    function formatPointType(type) {
      return type === 1 ? "获取" : type === 2 ? "消费" : "其他";
    }

    function formatCouponStatus(status) {
      var map = { 0: "未使用", 1: "已使用", 2: "已过期" };
      return map[status] || "未知";
    }

    function formatCouponType(type) {
      return type === 1 ? "满减券" : type === 2 ? "折扣券" : "优惠券";
    }

    common_vendor.onShow(() => {
      loadData();
    });

    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(username.value),
        b: common_vendor.t(phone.value),
        c: common_vendor.t(isMember.value ? "VIP会员" : "普通用户"),
        d: isMember.value ? "vip-badge" : "normal-badge",
        e: common_vendor.t(pointValue.value),
        f: common_vendor.t(isMember.value ? memberExpire.value : "未开通"),
        g: isMember.value ? 1 : "",
        h: common_vendor.o(handleCheckIn),
        i: checkedIn.value ? "checked" : "",
        j: common_vendor.t(checkedIn.value ? "已签到" : "签到领积分"),
        k: common_vendor.t(checkedIn.value ? "✓" : "+10"),
        l: common_vendor.o(togglePointList),
        m: common_vendor.t(pointList.value.length > 0 ? pointList.value.length + "条记录" : "暂无记录"),
        n: common_vendor.o(handleActivate),
        o: activeTab.value === "coupons",
        p: common_vendor.o(($event) => switchTab("coupons")),
        q: activeTab.value === "templates",
        r: common_vendor.o(($event) => switchTab("templates")),
        s: myCoupons.value.length === 0,
        t: common_vendor.f(myCoupons.value, (coupon, k0, i0) => {
          return {
            a: common_vendor.t(coupon.templateName || "优惠券"),
            b: common_vendor.t(formatCouponType(coupon.templateType)),
            c: common_vendor.t(coupon.templateType === 1 ? "满" + coupon.templateMinAmount + "减" + coupon.templateDiscountValue : coupon.templateDiscountValue + "折"),
            d: common_vendor.t(formatCouponStatus(coupon.status)),
            e: coupon.status === 1 || coupon.status === 2,
            f: common_vendor.t(coupon.validStartTime || ""),
            g: common_vendor.t(coupon.validEndTime || ""),
            h: k0
          };
        }),
        u: activeTab.value === "templates",
        v: couponTemplates.value.length === 0,
        w: common_vendor.f(couponTemplates.value, (tpl, k0, i0) => {
          return {
            a: common_vendor.t(tpl.name),
            b: common_vendor.t(formatCouponType(tpl.type)),
            c: common_vendor.t(tpl.type === 1 ? "满" + tpl.minAmount + "减" + tpl.discountValue : tpl.discountValue + "折"),
            d: common_vendor.t(tpl.validDays ? tpl.validDays + "天" : ""),
            e: k0,
            f: common_vendor.o(($event) => handleReceiveCoupon(tpl.id), k0)
          };
        }),
        x: showPointList.value ? 1 : "",
        y: common_vendor.o(togglePointList),
        z: common_vendor.o(() => {}),
        A: pointList.value.length === 0,
        B: common_vendor.f(pointList.value, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.point > 0 ? "+" + item.point : String(item.point)),
            b: item.point > 0 ? "point-add" : "point-sub",
            c: common_vendor.t(item.source || ""),
            d: common_vendor.t(formatPointType(item.type)),
            e: common_vendor.t(item.createTime || ""),
            f: k0
          };
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-member001"]]);
wx.createPage(MiniProgramPage);
