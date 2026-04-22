"use strict";
const common_vendor = require("../../common/vendor.js");
const stores_user = require("../../stores/user.js");
const _sfc_main = {
  __name: "login",
  setup(__props) {
    const userStore = stores_user.useUserStore();
    const isRegister = common_vendor.ref(false);
    const isLoading = common_vendor.ref(false);
    const errorMessage = common_vendor.ref("");
    const form = common_vendor.ref({
      username: "",
      password: "",
      phone: ""
    });
    const testAccounts = [
      { username: "admin", role: "管理员" },
      { username: "cashier", role: "收银员" },
      { username: "kitchen", role: "后厨" },
      { username: "customer", role: "顾客" }
    ];
    const switchTab = (val) => {
      isRegister.value = val;
      errorMessage.value = "";
    };
    const onInputUsername = (e) => {
      form.value.username = e.detail.value;
    };
    const onInputPassword = (e) => {
      form.value.password = e.detail.value;
    };
    const onInputPhone = (e) => {
      form.value.phone = e.detail.value;
    };
    const fillAccount = (acc) => {
      form.value.username = acc.username;
      form.value.password = "123456";
    };
    const handleSubmit = () => {
      errorMessage.value = "";
      if (!form.value.username || !form.value.password) {
        errorMessage.value = "请填写完整信息";
        return;
      }
      if (isRegister.value && !form.value.phone) {
        errorMessage.value = "请填写手机号";
        return;
      }
      isLoading.value = true;
      const loginData = {
        username: form.value.username,
        password: form.value.password
      };
      if (isRegister.value) {
        loginData.phone = form.value.phone;
      }
      const handleLoginResult = (result) => {
        isLoading.value = false;
        if (result.success) {
          common_vendor.index.showToast({ title: "登录成功", icon: "success" });
          setTimeout(() => {
            common_vendor.index.switchTab({ url: "/pages/index/index" });
          }, 500);
        } else {
          errorMessage.value = result.message;
        }
      };
      if (isRegister.value) {
        userStore.register(loginData).then(handleLoginResult).catch((err) => {
          isLoading.value = false;
          errorMessage.value = "操作失败，请重试";
          console.error("[Login Error]", err);
        });
      } else {
        console.log("[Login] 开始登录请求, 用户名:", loginData.username);
        userStore.login(loginData).then(handleLoginResult).catch((err) => {
          isLoading.value = false;
          errorMessage.value = "操作失败，请重试";
          console.error("[Login Error]", err);
        });
      }
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !isRegister.value ? 1 : "",
        b: common_vendor.o(($event) => switchTab(false)),
        c: isRegister.value ? 1 : "",
        d: common_vendor.o(($event) => switchTab(true)),
        e: form.value.username,
        f: common_vendor.o(onInputUsername),
        g: form.value.password,
        h: common_vendor.o(onInputPassword),
        i: isRegister.value
      }, isRegister.value ? {
        j: form.value.phone,
        k: common_vendor.o(onInputPhone)
      } : {}, {
        l: errorMessage.value
      }, errorMessage.value ? {
        m: common_vendor.t(errorMessage.value)
      } : {}, {
        n: common_vendor.t(isLoading.value ? isRegister.value ? "注册中..." : "登录中..." : isRegister.value ? "注册" : "登录"),
        o: isLoading.value ? 1 : "",
        p: common_vendor.o(handleSubmit),
        q: !isRegister.value
      }, !isRegister.value ? {
        r: common_vendor.f(testAccounts, (acc, i, i0) => {
          return {
            a: common_vendor.t(acc.role),
            b: common_vendor.t(acc.username),
            c: i,
            d: common_vendor.o(($event) => fillAccount(acc), i)
          };
        })
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-cdfe2409"]]);
wx.createPage(MiniProgramPage);
