var imgUrl =
  "https://charging-hz-oss-bucket.oss-cn-hangzhou.aliyuncs.com/static/";
// 100Charge
var appName = "100Charge"
var appid = "wx361b3b951a7d5f54" // 微信appid
var api = "http://localhost:8080/"; // API接口地址
var qqMapKey = "5E2BZ-ZDS3L-3JEPJ-MYBKS-ZCMHS-EEF7A" // 地图Api key



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