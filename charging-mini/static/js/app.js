var imgUrl =
  "https://charging-hz-oss-bucket.oss-cn-hangzhou.aliyuncs.com/static/";
// 100Charge
var appName = "100Charge"
var appid = "wx361b3b951a7d5f54"
var api = "http://192.168.110.135:8083/"; // api
var qqMapKey = "5E2BZ-ZDS3L-3JEPJ-MYBKS-ZCMHS-EEF7A" // 王腾



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