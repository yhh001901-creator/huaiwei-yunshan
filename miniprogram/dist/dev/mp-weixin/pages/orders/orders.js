"use strict";
const common_vendor = require("../../common/vendor.js");
const utils_request = require("../../utils/request.js");
const _sfc_main = {
  __name: "orders",
  setup(__props) {
    const loading = common_vendor.ref(true);
    const error = common_vendor.ref(false);
    const orders = common_vendor.ref([]);
    const showDetailModal = common_vendor.ref(false);
    const selectedOrder = common_vendor.ref(null);
    const orderDetails = common_vendor.ref([]);
    const completedCount = common_vendor.computed(() => {
      return orders.value.filter((o) => o.status === 3).length;
    });
    function getStatusText(status) {
      const map = { 0: "待付款", 1: "待接单", 2: "制作中", 3: "已完成", 4: "已取消" };
      return map[status] || "未知";
    }
    function formatTime(time) {
      if (!time) return "";
      const date = new Date(time);
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const day = date.getDate().toString().padStart(2, "0");
      const hour = date.getHours().toString().padStart(2, "0");
      const minute = date.getMinutes().toString().padStart(2, "0");
      return `${month}-${day} ${hour}:${minute}`;
    }
    function goToOrder() {
      common_vendor.index.switchTab({ url: "/pages/index/index" });
    }
    async function fetchOrders() {
      loading.value = true;
      error.value = false;
      try {
        const data = await utils_request.get("/api/orders/list");
        orders.value = (data || []).sort((a, b) => new Date(b.createTime) - new Date(a.createTime));
      } catch (e) {
        console.error("获取订单失败:", e);
        error.value = true;
      } finally {
        loading.value = false;
      }
    }
    async function showOrderDetail(order) {
      selectedOrder.value = order;
      showDetailModal.value = true;
      try {
        const data = await utils_request.get("/api/orders/" + order.orderNo);
        orderDetails.value = (data == null ? void 0 : data.orderDetails) || [];
      } catch (e) {
        console.error("获取订单详情失败:", e);
        orderDetails.value = [];
      }
    }
    common_vendor.onShow(() => {
      fetchOrders();
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e, _f;
      return common_vendor.e({
        a: loading.value
      }, loading.value ? {} : error.value ? {
        c: common_vendor.o(fetchOrders)
      } : orders.value.length === 0 ? {
        e: common_vendor.o(goToOrder)
      } : {
        f: common_vendor.t(orders.value.length),
        g: common_vendor.t(completedCount.value),
        h: common_vendor.f(orders.value, (order, k0, i0) => {
          return {
            a: common_vendor.t(order.tableNo || "外带"),
            b: common_vendor.t(getStatusText(order.status)),
            c: common_vendor.n("status-" + order.status),
            d: common_vendor.t(order.orderNo),
            e: common_vendor.t(formatTime(order.createTime)),
            f: common_vendor.t(parseFloat(order.totalAmount).toFixed(2)),
            g: order.id,
            h: common_vendor.n("card-" + order.status),
            i: common_vendor.o(($event) => showOrderDetail(order), order.id)
          };
        })
      }, {
        b: error.value,
        d: orders.value.length === 0,
        i: showDetailModal.value
      }, showDetailModal.value ? {
        j: common_vendor.t(getStatusText((_a = selectedOrder.value) == null ? void 0 : _a.status)),
        k: common_vendor.n("status-" + ((_b = selectedOrder.value) == null ? void 0 : _b.status)),
        l: common_vendor.o(($event) => showDetailModal.value = false),
        m: common_vendor.t((_c = selectedOrder.value) == null ? void 0 : _c.orderNo),
        n: common_vendor.t(((_d = selectedOrder.value) == null ? void 0 : _d.tableNo) || "外带"),
        o: common_vendor.t((_e = selectedOrder.value) == null ? void 0 : _e.createTime),
        p: common_vendor.t(orderDetails.value.length),
        q: common_vendor.f(orderDetails.value, (item, index, i0) => {
          return {
            a: common_vendor.t(item.dishName),
            b: common_vendor.t(item.unitPrice),
            c: common_vendor.t(item.quantity),
            d: common_vendor.t((item.unitPrice * item.quantity).toFixed(2)),
            e: index
          };
        }),
        r: common_vendor.t(parseFloat(((_f = selectedOrder.value) == null ? void 0 : _f.totalAmount) || 0).toFixed(2)),
        s: common_vendor.o(() => {
        }),
        t: common_vendor.o(($event) => showDetailModal.value = false)
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-926fbf1b"]]);
wx.createPage(MiniProgramPage);
