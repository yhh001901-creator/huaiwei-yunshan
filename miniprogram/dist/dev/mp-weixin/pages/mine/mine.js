"use strict";
const common_vendor = require("../../common/vendor.js");
const stores_user = require("../../stores/user.js");
const stores_cart = require("../../stores/cart.js");
const utils_request = require("../../utils/request.js");
const _sfc_main = {
  __name: "mine",
  setup(__props) {
    const userStore = stores_user.useUserStore();
    const cartStore = stores_cart.useCartStore();
    const username = common_vendor.computed(() => {
      var _a;
      return ((_a = userStore.state.userInfo) == null ? void 0 : _a.username) || "用户";
    });
    const roleName = common_vendor.computed(() => {
      var _b;
      return ((_b = userStore.state.userInfo) == null ? void 0 : _b.roleName) || "顾客";
    });
    const cartCount = common_vendor.computed(() => cartStore.totalQuantity);
    const orderCount = common_vendor.ref(0);
    const showEditModal = common_vendor.ref(false);
    const showPasswordModal = common_vendor.ref(false);
    const showAboutModal = common_vendor.ref(false);
    const editForm = common_vendor.ref({ username: "", phone: "" });
    const pwdForm = common_vendor.ref({ oldPassword: "", newPassword: "", confirmPassword: "" });
    async function fetchOrderCount() {
      try {
        const data = await utils_request.get("/api/orders/list");
        orderCount.value = (data || []).length;
      } catch (e) {
        orderCount.value = 0;
      }
    }
    function goToOrders() {
      common_vendor.index.switchTab({ url: "/pages/orders/orders" });
    }
    function showStatsTip() {
      common_vendor.index.showToast({ title: "功能开发中...", icon: "none" });
    }
    function showAvatarPicker() {
      common_vendor.index.showToast({ title: "头像功能开发中...", icon: "none" });
    }
    function handleLogout() {
      common_vendor.index.showModal({
        title: "退出登录",
        content: "确定要退出登录吗？",
        confirmColor: "#F97316",
        success: (res) => {
          if (res.confirm) {
            userStore.logout();
            common_vendor.index.reLaunch({ url: "/pages/login/login" });
          }
        }
      });
    }
    function saveProfile() {
      if (!editForm.value.username && !editForm.value.phone) {
        common_vendor.index.showToast({ title: "请填写要修改的内容", icon: "none" });
        return;
      }
      common_vendor.index.showLoading({ title: "保存中..." });
      const data = {};
      if (editForm.value.username) data.username = editForm.value.username;
      if (editForm.value.phone) data.phone = editForm.value.phone;
      userStore.updateProfile(data).then((res) => {
        common_vendor.index.hideLoading();
        if (res.success) {
          showEditModal.value = false;
          common_vendor.index.showToast({ title: "修改成功", icon: "success" });
        } else {
          common_vendor.index.showToast({ title: res.message || "修改失败", icon: "none" });
        }
      }).catch(() => {
        common_vendor.index.hideLoading();
        common_vendor.index.showToast({ title: "修改失败", icon: "none" });
      });
    }
    function savePassword() {
      if (!pwdForm.value.oldPassword) {
        common_vendor.index.showToast({ title: "请输入旧密码", icon: "none" });
        return;
      }
      if (!pwdForm.value.newPassword) {
        common_vendor.index.showToast({ title: "请输入新密码", icon: "none" });
        return;
      }
      if (pwdForm.value.newPassword !== pwdForm.value.confirmPassword) {
        common_vendor.index.showToast({ title: "两次密码不一致", icon: "none" });
        return;
      }
      if (pwdForm.value.newPassword.length < 6) {
        common_vendor.index.showToast({ title: "密码不能少于6位", icon: "none" });
        return;
      }
      common_vendor.index.showLoading({ title: "修改中..." });
      userStore.updatePassword(pwdForm.value.oldPassword, pwdForm.value.newPassword).then((res) => {
        common_vendor.index.hideLoading();
        if (res.success) {
          showPasswordModal.value = false;
          pwdForm.value = { oldPassword: "", newPassword: "", confirmPassword: "" };
          common_vendor.index.showToast({ title: "密码修改成功", icon: "success" });
        } else {
          common_vendor.index.showToast({ title: res.message || "修改失败", icon: "none" });
        }
      }).catch(() => {
        common_vendor.index.hideLoading();
        common_vendor.index.showToast({ title: "修改失败", icon: "none" });
      });
    }
    function initEditForm() {
      var _a, _b;
      editForm.value = {
        username: ((_a = userStore.state.userInfo) == null ? void 0 : _a.username) || "",
        phone: ((_b = userStore.state.userInfo) == null ? void 0 : _b.phone) || ""
      };
    }
    common_vendor.onShow(() => {
      fetchOrderCount();
    });
    common_vendor.onMounted(() => {
      initEditForm();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(username.value.charAt(0).toUpperCase()),
        b: common_vendor.o(showAvatarPicker),
        c: common_vendor.t(username.value),
        d: common_vendor.t(roleName.value),
        e: common_vendor.o(($event) => showEditModal.value = true),
        f: common_vendor.t(orderCount.value),
        g: common_vendor.o(goToOrders),
        h: common_vendor.t(cartCount.value),
        i: common_vendor.o(showStatsTip),
        j: common_vendor.o(($event) => showEditModal.value = true),
        k: common_vendor.o(($event) => showPasswordModal.value = true),
        l: common_vendor.o(($event) => showAboutModal.value = true),
        m: common_vendor.o(handleLogout),
        n: showEditModal.value
      }, showEditModal.value ? {
        o: common_vendor.o(($event) => showEditModal.value = false),
        p: editForm.value.username,
        q: common_vendor.o(($event) => editForm.value.username = $event.detail.value),
        r: editForm.value.phone,
        s: common_vendor.o(($event) => editForm.value.phone = $event.detail.value),
        t: common_vendor.o(($event) => showEditModal.value = false),
        v: common_vendor.o(saveProfile),
        w: common_vendor.o(() => {
        }),
        x: common_vendor.o(($event) => showEditModal.value = false)
      } : {}, {
        y: showPasswordModal.value
      }, showPasswordModal.value ? {
        z: common_vendor.o(($event) => showPasswordModal.value = false),
        A: pwdForm.value.oldPassword,
        B: common_vendor.o(($event) => pwdForm.value.oldPassword = $event.detail.value),
        C: pwdForm.value.newPassword,
        D: common_vendor.o(($event) => pwdForm.value.newPassword = $event.detail.value),
        E: pwdForm.value.confirmPassword,
        F: common_vendor.o(($event) => pwdForm.value.confirmPassword = $event.detail.value),
        G: common_vendor.o(($event) => showPasswordModal.value = false),
        H: common_vendor.o(savePassword),
        I: common_vendor.o(() => {
        }),
        J: common_vendor.o(($event) => showPasswordModal.value = false)
      } : {}, {
        K: showAboutModal.value
      }, showAboutModal.value ? {
        L: common_vendor.o(($event) => showAboutModal.value = false),
        M: common_vendor.o(($event) => showAboutModal.value = false),
        N: common_vendor.o(() => {
        }),
        O: common_vendor.o(($event) => showAboutModal.value = false)
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-d41d38da"]]);
wx.createPage(MiniProgramPage);
