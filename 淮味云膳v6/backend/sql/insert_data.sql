-- 使用数据库
USE `huaiwei-v2`;

-- 插入分类数据
INSERT INTO category (name, sort) VALUES
('精品淮扬菜', 1),
('主食', 2),
('饮品', 3),
('甜点', 4);

-- 插入精品淮扬菜
INSERT INTO dish (category_id, name, price, image_url, description, status) VALUES
(1, '红烧肉', 68.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=braised%20pork%20belly%20chinese%20cuisine&size=512x512', '传统红烧肉，肥而不腻', 1),
(1, '清蒸鲈鱼', 88.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=steamed%20bass%20fish%20chinese%20cuisine&size=512x512', '新鲜鲈鱼，清蒸保留原味', 1),
(1, '宫保鸡丁', 48.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=kung%20pao%20chicken%20chinese%20cuisine&size=512x512', '经典川菜，香辣可口', 1),
(1, '鱼香肉丝', 42.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=fish%20fragrant%20shredded%20pork%20chinese%20cuisine&size=512x512', '酸甜可口，下饭神器', 1),
(1, '麻婆豆腐', 38.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=mapo%20tofu%20chinese%20cuisine&size=512x512', '川菜经典，麻辣鲜香', 1),
(1, '糖醋排骨', 58.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=sweet%20and%20sour%20spare%20ribs%20chinese%20cuisine&size=512x512', '酸甜适中，口感酥脆', 1),
(1, '水煮鱼', 78.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=boiled%20fish%20sichuan%20cuisine&size=512x512', '麻辣鲜香，鱼肉鲜嫩', 1),
(1, '梅菜扣肉', 62.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=steamed%20pork%20with%20preserved%20vegetables&size=512x512', '肥而不腻，梅菜香味浓郁', 1),
(1, '回锅肉', 46.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=twice%20cooked%20pork%20sichuan%20cuisine&size=512x512', '川菜经典，香辣可口', 1),
(1, '蒜蓉西兰花', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=garlic%20broccoli%20chinese%20style&size=512x512', '清爽健康，营养丰富', 1),
(1, '青椒肉丝', 38.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=green%20pepper%20shredded%20pork%20chinese%20cuisine&size=512x512', '青椒脆爽，肉丝嫩滑', 1),
(1, '酸辣土豆丝', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=sour%20and%20spicy%20shredded%20potatoes%20chinese%20cuisine&size=512x512', '酸辣可口，开胃下饭', 1),
(1, '可乐鸡翅', 58.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=cola%20chicken%20wings%20chinese%20cuisine&size=512x512', '甜香可口，老少皆宜', 1),
(1, '西红柿炒蛋', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=tomato%20and%20egg%20stir%20fry%20chinese%20cuisine&size=512x512', '经典家常菜，酸甜可口', 1),
(1, '酸辣汤', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=sour%20and%20spicy%20soup%20chinese%20cuisine&size=512x512', '酸辣开胃，温暖身心', 1),
(1, '蒜蓉扇贝', 78.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=garlic%20scallops%20chinese%20cuisine&size=512x512', '新鲜扇贝，蒜香浓郁', 1),
(1, '椒盐虾', 68.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=spicy%20salt%20shrimp%20chinese%20cuisine&size=512x512', '外酥里嫩，椒盐味浓', 1),
(1, '白切鸡', 58.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=white%20cut%20chicken%20cantonese%20cuisine&size=512x512', '皮滑肉嫩，原汁原味', 1),
(1, '红烧肉圆', 48.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=braised%20pork%20meatballs%20chinese%20cuisine&size=512x512', '肉质鲜美，汤汁浓郁', 1),
(1, '清炒时蔬', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=stir%20fried%20vegetables%20chinese%20style&size=512x512', '清爽健康，营养丰富', 1);

-- 插入主食菜品
INSERT INTO dish (category_id, name, price, image_url, description, status) VALUES
(2, '扬州炒饭', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=yangzhou%20fried%20rice%20chinese%20cuisine&size=512x512', '经典扬州炒饭，粒粒分明', 1),
(2, '红烧牛肉面', 38.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=braised%20beef%20noodle%20soup&size=512x512', '牛肉软烂，汤汁浓郁', 1),
(2, '炸酱面', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=zhajiang%20noodles%20beijing%20cuisine&size=512x512', '传统北京炸酱面', 1),
(2, '担担面', 30.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=dandan%20noodles%20sichuan%20cuisine&size=512x512', '四川担担面，麻辣鲜香', 1),
(2, '小笼包', 36.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=xiaolongbao%20steamed%20dumplings&size=512x512', '皮薄馅大，汤汁鲜美', 1),
(2, '生煎包', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=pan%20fried%20buns%20chinese%20cuisine&size=512x512', '底部酥脆，肉馅多汁', 1),
(2, '饺子', 38.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20dumplings%20jiaozi&size=512x512', '传统手工饺子', 1),
(2, '混沌', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=wonton%20soup%20chinese%20cuisine&size=512x512', '皮薄馅嫩，汤汁鲜美', 1),
(2, '米饭', 5.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=steamed%20rice%20chinese%20style&size=512x512', '优质大米，粒粒饱满', 1),
(2, '馒头', 3.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=steamed%20buns%20chinese%20style&size=512x512', '传统手工馒头', 1),
(2, '蛋炒饭', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=egg%20fried%20rice%20chinese%20cuisine&size=512x512', '经典蛋炒饭，香气四溢', 1),
(2, '番茄鸡蛋面', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=tomato%20and%20egg%20noodles%20chinese%20cuisine&size=512x512', '酸甜可口，营养丰富', 1),
(2, '葱油拌面', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=scallion%20oil%20noodles%20chinese%20cuisine&size=512x512', '葱香浓郁，口感劲道', 1),
(2, '刀削面', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=knife%20cut%20noodles%20chinese%20cuisine&size=512x512', '手工刀削，口感独特', 1),
(2, '灌汤包', 38.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=soup%20dumplings%20chinese%20cuisine&size=512x512', '汤汁饱满，鲜香可口', 1),
(2, '肉夹馍', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=roujiamo%20chinese%20burger&size=512x512', '传统陕西美食，肉香四溢', 1),
(2, '炒面', 30.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chow%20mein%20fried%20noodles&size=512x512', '口感丰富，香气扑鼻', 1),
(2, '腊八粥', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=lab%20a%20zhu%20congee%20chinese%20cuisine&size=512x512', '营养丰富，暖身暖心', 1),
(2, '小米粥', 15.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=millet%20porridge%20chinese%20style&size=512x512', '健脾养胃，清淡可口', 1),
(2, '油条', 8.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=youtiao%20fried%20dough%20stick&size=512x512', '传统早餐，外酥里嫩', 1);

-- 插入饮品
INSERT INTO dish (category_id, name, price, image_url, description, status) VALUES
(3, '可乐', 10.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=coca%20cola%20drink&size=512x512', '冰镇可乐', 1),
(3, '雪碧', 10.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=sprite%20drink&size=512x512', '冰镇雪碧', 1),
(3, '芬达', 10.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=fanta%20orange%20drink&size=512x512', '冰镇芬达', 1),
(3, '矿泉水', 5.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=mineral%20water%20bottle&size=512x512', '纯净水', 1),
(3, '绿茶', 15.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=green%20tea%20chinese%20style&size=512x512', '清香绿茶', 1),
(3, '红茶', 15.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=black%20tea%20chinese%20style&size=512x512', '浓郁红茶', 1),
(3, '茉莉花茶', 18.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=jasmine%20tea%20chinese%20style&size=512x512', '花香四溢', 1),
(3, '奶茶', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=milk%20tea%20chinese%20style&size=512x512', '香甜奶茶', 1),
(3, '果汁', 20.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=fresh%20fruit%20juice&size=512x512', '新鲜果汁', 1),
(3, '啤酒', 15.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=beer%20glass&size=512x512', '冰镇啤酒', 1),
(3, '橙汁', 18.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=orange%20juice%20fresh&size=512x512', '新鲜橙汁，富含维C', 1),
(3, '苹果汁', 18.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=apple%20juice%20fresh&size=512x512', '新鲜苹果汁，口感清甜', 1),
(3, '芒果汁', 22.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=mango%20juice%20fresh&size=512x512', '新鲜芒果汁，果香浓郁', 1),
(3, '柠檬汁', 20.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=lemon%20juice%20fresh&size=512x512', '新鲜柠檬汁，酸爽可口', 1),
(3, '西瓜汁', 20.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=watermelon%20juice%20fresh&size=512x512', '新鲜西瓜汁，清凉解渴', 1),
(3, '冰咖啡', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=iced%20coffee%20drink&size=512x512', '冰镇咖啡，提神醒脑', 1),
(3, '热咖啡', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=hot%20coffee%20drink&size=512x512', '热咖啡，温暖身心', 1),
(3, '豆浆', 12.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=soybean%20milk%20chinese%20style&size=512x512', '现磨豆浆，营养丰富', 1),
(3, '酸梅汤', 15.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=plum%20juice%20chinese%20style&size=512x512', '传统酸梅汤，酸甜可口', 1),
(3, '蜂蜜柚子茶', 22.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=honey%20grapefruit%20tea&size=512x512', '蜂蜜柚子茶，滋润养颜', 1);

-- 插入甜点
INSERT INTO dish (category_id, name, price, image_url, description, status) VALUES
(4, '提拉米苏', 38.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=tiramisu%20dessert&size=512x512', '经典意式甜点', 1),
(4, '巧克力蛋糕', 42.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chocolate%20cake%20dessert&size=512x512', '浓郁巧克力味', 1),
(4, '草莓蛋糕', 45.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=strawberry%20cake%20dessert&size=512x512', '新鲜草莓', 1),
(4, '芝士蛋糕', 40.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=cheesecake%20dessert&size=512x512', '浓郁芝士味', 1),
(4, '芒果布丁', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=mango%20pudding%20dessert&size=512x512', '香甜芒果味', 1),
(4, '冰淇淋', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=ice%20cream%20dessert&size=512x512', '多种口味', 1),
(4, '糯米糍', 22.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=glutinous%20rice%20cake%20chinese%20dessert&size=512x512', '软糯香甜', 1),
(4, '绿豆糕', 20.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=mung%20bean%20cake%20chinese%20dessert&size=512x512', '传统中式甜点', 1),
(4, '银耳羹', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=white%20fungus%20soup%20chinese%20dessert&size=512x512', '滋润养颜', 1),
(4, '双皮奶', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=double%20skin%20milk%20dessert&size=512x512', '传统广东甜点', 1),
(4, '黑森林蛋糕', 48.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=black%20forest%20cake%20dessert&size=512x512', '经典德式甜点，巧克力与樱桃的完美结合', 1),
(4, '抹茶蛋糕', 42.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=matcha%20cake%20dessert&size=512x512', '日式抹茶风味，口感细腻', 1),
(4, '蓝莓蛋糕', 45.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=blueberry%20cake%20dessert&size=512x512', '新鲜蓝莓，酸甜可口', 1),
(4, '焦糖布丁', 32.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=creme%20brulee%20dessert&size=512x512', '法式焦糖布丁，口感丝滑', 1),
(4, '水果沙拉', 35.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=fruit%20salad%20dessert&size=512x512', '新鲜水果，营养健康', 1),
(4, '椰丝糯米糍', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=coconut%20glutinous%20rice%20cake&size=512x512', '椰香浓郁，软糯香甜', 1),
(4, '红豆沙', 22.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=red%20bean%20paste%20chinese%20dessert&size=512x512', '传统中式甜点，红豆香甜', 1),
(4, '芝麻糊', 20.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=sesame%20paste%20chinese%20dessert&size=512x512', '香浓芝麻味，营养丰富', 1),
(4, '杏仁豆腐', 28.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=almond%20tofu%20chinese%20dessert&size=512x512', '口感滑嫩，杏仁香浓郁', 1),
(4, '桂花糕', 25.00, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=osmanthus%20cake%20chinese%20dessert&size=512x512', '桂花香气扑鼻，口感软糯', 1);
