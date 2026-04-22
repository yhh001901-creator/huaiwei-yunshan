"use strict";
const common_vendor = require("../common/vendor.js");
const CART_STORAGE_KEY = "cart_items";
const state = common_vendor.reactive({
  items: JSON.parse(common_vendor.index.getStorageSync(CART_STORAGE_KEY) || "[]"),
  tableNo: common_vendor.index.getStorageSync("cart_table_no") || ""
});
function persist() {
  common_vendor.index.setStorageSync(CART_STORAGE_KEY, JSON.stringify(state.items));
  common_vendor.index.setStorageSync("cart_table_no", state.tableNo);
}
function useCartStore() {
  const totalQuantity = common_vendor.computed(() => state.items.reduce((t, item) => t + item.quantity, 0));
  const totalPrice = common_vendor.computed(() => state.items.reduce((t, item) => t + item.price * item.quantity, 0));
  const itemCount = common_vendor.computed(() => state.items.length);
  function addItem(dish) {
    const existing = state.items.find((item) => item.id === dish.id);
    if (existing) {
      if (existing.quantity < dish.stock) {
        existing.quantity++;
      } else {
        common_vendor.index.showToast({ title: "已达到库存上限", icon: "none" });
        return;
      }
    } else {
      state.items.push({
        id: dish.id,
        name: dish.name,
        price: dish.price,
        imageUrl: dish.imageUrl,
        quantity: 1,
        stock: dish.stock
      });
    }
    persist();
  }
  function removeItem(index) {
    state.items.splice(index, 1);
    persist();
  }
  function increaseQuantity(index) {
    const item = state.items[index];
    if (item.quantity < item.stock) {
      item.quantity++;
      persist();
    } else {
      common_vendor.index.showToast({ title: "已达到库存上限", icon: "none" });
    }
  }
  function decreaseQuantity(index) {
    if (state.items[index].quantity > 1) {
      state.items[index].quantity--;
    } else {
      removeItem(index);
      return;
    }
    persist();
  }
  function clearCart() {
    state.items = [];
    state.tableNo = "";
    persist();
  }
  function setTableNo(table) {
    state.tableNo = table;
    persist();
  }
  function getOrderDetails() {
    return state.items.map((item) => ({
      dishId: item.id,
      dishName: item.name,
      quantity: item.quantity,
      unitPrice: item.price
    }));
  }
  return {
    items: state.items,
    tableNo: state.tableNo,
    totalQuantity,
    totalPrice,
    itemCount,
    addItem,
    removeItem,
    increaseQuantity,
    decreaseQuantity,
    clearCart,
    setTableNo,
    getOrderDetails
  };
}
exports.useCartStore = useCartStore;
