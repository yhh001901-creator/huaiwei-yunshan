"use strict";
const common_vendor = require("../../common/vendor.js");
const stores_user = require("../../stores/user.js");
const stores_cart = require("../../stores/cart.js");
const utils_request = require("../../utils/request.js");
const placeholderImage = "/static/placeholder.svg";
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const userStore = stores_user.useUserStore();
    const cartStore = stores_cart.useCartStore();
    const username = common_vendor.computed(() => userStore.username || "");
    const loading = common_vendor.ref(true);
    const error = common_vendor.ref(false);
    const categories = common_vendor.ref([]);
    const dishes = common_vendor.ref([]);
    const selectedCategory = common_vendor.ref(null);
    const showCart = common_vendor.ref(false);
    const showTablePicker = common_vendor.ref(false);
    const tables = ["A01", "A02", "A03", "A04", "A05", "A06", "A07", "A08", "A09", "A10"];
    const cartItems = common_vendor.computed(() => cartStore.items);
    const cartItemCount = common_vendor.computed(() => cartStore.totalQuantity);
    const cartTotalPrice = common_vendor.computed(() => cartStore.totalPrice);
    const tableNo = common_vendor.computed(() => cartStore.tableNo);
    const filteredDishes = common_vendor.computed(() => {
      if (!selectedCategory.value) return dishes.value.filter((d) => d.status === 1);
      return dishes.value.filter((d) => d.categoryId === selectedCategory.value.id && d.status === 1);
    });
    function getImageUrl(dish) {
      if (!dish.imageUrl) return placeholderImage;
      const url = dish.imageUrl.startsWith("/") ? dish.imageUrl : "/" + dish.imageUrl;
      return utils_request.imageBaseURL + url;
    }
    function onImageError(dish) {
      dish._imageError = true;
    }
    function onCartItemImageError(index) {
      cartStore.items[index]._imageLoaded = false;
    }
    function onCartItemImageLoad(index) {
      cartStore.items[index]._imageLoaded = true;
    }
    async function loadData() {
      loading.value = true;
      error.value = false;
      try {
        const [catData, dishData] = await Promise.all([
          utils_request.get("/api/category/list"),
          utils_request.get("/api/dish/list")
        ]);
        categories.value = catData || [];
        dishes.value = (dishData || []).map((d) => ({
          ...d,
          _imageError: false
        }));
        if (categories.value.length > 0) {
          selectedCategory.value = categories.value[0];
        }
      } catch (e) {
        console.error("加载数据失败:", e);
        error.value = true;
      } finally {
        loading.value = false;
      }
    }
    function selectCategory(cat) {
      selectedCategory.value = cat;
    }
    function addToCart(dish) {
      cartStore.addItem(dish);
      common_vendor.index.showToast({ title: "已加入购物车", icon: "success", duration: 1e3 });
    }
    function selectTable(t) {
      cartStore.setTableNo(t);
      showTablePicker.value = false;
    }
    async function submitOrder() {
      var _a;
      if (!tableNo.value || cartItems.value.length === 0) return;
      try {
        const orderData = {
          userId: (_a = userStore.state.userInfo) == null ? void 0 : _a.userId,
          totalAmount: cartTotalPrice.value,
          tableNo: tableNo.value,
          orderDetails: cartStore.getOrderDetails()
        };
        await utils_request.post("/api/orders", orderData);
        cartStore.clearCart();
        showCart.value = false;
        common_vendor.index.showToast({ title: "订单提交成功！", icon: "success" });
      } catch (e) {
        console.error("提交订单失败:", e);
        common_vendor.index.showToast({ title: "提交订单失败", icon: "none" });
      }
    }
    common_vendor.onMounted(() => {
      loadData();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(username.value),
        b: loading.value
      }, loading.value ? {} : error.value ? {
        d: common_vendor.o(loadData)
      } : common_vendor.e({
        e: common_vendor.f(categories.value, (cat, k0, i0) => {
          var _a;
          return {
            a: common_vendor.t(cat.name),
            b: cat.id,
            c: ((_a = selectedCategory.value) == null ? void 0 : _a.id) === cat.id ? 1 : "",
            d: common_vendor.o(($event) => selectCategory(cat), cat.id)
          };
        }),
        f: filteredDishes.value.length === 0
      }, filteredDishes.value.length === 0 ? {} : {
        g: common_vendor.f(filteredDishes.value, (dish, k0, i0) => {
          return {
            a: getImageUrl(dish),
            b: common_vendor.o(($event) => onImageError(dish), dish.id),
            c: common_vendor.t(dish.name),
            d: common_vendor.t(dish.price),
            e: common_vendor.t(dish.stock <= 0 ? "已售罄" : dish.stock <= 5 ? "库存紧张" : "库存：" + dish.stock),
            f: common_vendor.n(dish.stock <= 0 ? "out" : dish.stock <= 5 ? "low" : ""),
            g: common_vendor.t(dish.status === 1 && dish.stock > 0 ? "加入购物车" : "已售罄"),
            h: dish.status === 0 || dish.stock <= 0 ? 1 : "",
            i: dish.status === 0 || dish.stock <= 0,
            j: common_vendor.o(($event) => addToCart(dish), dish.id),
            k: dish.id
          };
        })
      }), {
        c: error.value,
        h: showCart.value
      }, showCart.value ? common_vendor.e({
        i: common_vendor.o(($event) => showCart.value = false),
        j: cartItems.value.length === 0
      }, cartItems.value.length === 0 ? {} : {}, {
        k: common_vendor.f(cartItems.value, (item, index, i0) => {
          return {
            a: item._imageLoaded ? item.imageUrl : placeholderImage,
            b: common_vendor.o(($event) => onCartItemImageError(index), index),
            c: common_vendor.o(($event) => onCartItemImageLoad(index), index),
            d: common_vendor.t(item.name),
            e: common_vendor.t(item.price),
            f: common_vendor.o(($event) => common_vendor.unref(cartStore).decreaseQuantity(index), index),
            g: common_vendor.t(item.quantity),
            h: common_vendor.o(($event) => common_vendor.unref(cartStore).increaseQuantity(index), index),
            i: index
          };
        }),
        l: cartItems.value.length > 0
      }, cartItems.value.length > 0 ? {
        m: common_vendor.t(tableNo.value || "请选择桌号"),
        n: common_vendor.n(tableNo.value ? "selected" : "placeholder"),
        o: common_vendor.o(($event) => showTablePicker.value = true),
        p: common_vendor.t(cartTotalPrice.value.toFixed(2)),
        q: !tableNo.value,
        r: common_vendor.o(submitOrder)
      } : {}, {
        s: common_vendor.o(() => {
        }),
        t: common_vendor.o(($event) => showCart.value = false)
      }) : {}, {
        v: showTablePicker.value
      }, showTablePicker.value ? {
        w: common_vendor.o(($event) => showTablePicker.value = false),
        x: common_vendor.f(tables, (t, k0, i0) => {
          return {
            a: common_vendor.t(t),
            b: t,
            c: tableNo.value === t ? 1 : "",
            d: common_vendor.o(($event) => selectTable(t), t)
          };
        }),
        y: common_vendor.o(() => {
        }),
        z: common_vendor.o(($event) => showTablePicker.value = false)
      } : {}, {
        A: cartItemCount.value > 0
      }, cartItemCount.value > 0 ? {
        B: common_vendor.t(cartItemCount.value),
        C: common_vendor.t(cartItemCount.value),
        D: common_vendor.t(cartTotalPrice.value.toFixed(2)),
        E: common_vendor.o(($event) => showCart.value = true),
        F: common_vendor.o(($event) => showCart.value = true),
        G: showCart.value ? 1 : ""
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-83a5a03c"]]);
wx.createPage(MiniProgramPage);
