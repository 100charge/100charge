var imgUrl =
  "https://charging-hz-oss-bucket.oss-cn-hangzhou.aliyuncs.com/static/";
// 100Charge
var appid = "wxa590a3e7e6962b56" // 开充云
// 测试
var api = "https://wxapi.kaichongcharge.com/"; // 开充
// var api = "https://devwxapi.kaichongcharge.com/"; // 开充
// var api = "http://140.143.191.24:9080/"; // 测试api
// var appid = "wxe22e0fa150c28cec"; // 智碳充电


function toDecimal2(x) {
  var f = parseFloat(x);
  if (isNaN(f)) {
    return "0.00";
  }
  var f = Math.round(x * 100) / 100;
  var s = f.toString();
  var rs = s.indexOf(".");
  if (rs < 0) {
    rs = s.length;
    s += ".";
  }
  while (s.length <= rs + 2) {
    s += "0";
  }
  return s;
}
export default {
  api,
  imgUrl,
  appid,
  toDecimal2,
};