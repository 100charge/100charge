const fs = require('fs');
const path = require('path');

// 需要优化的大文件列表
const largeFiles = [
    'static/img/banner.png',
    'static/img/charging.gif',
    'static/img/3.png',
    'static/img/default-avatar.png',
    'static/img/charging-car.png',
    'static/img/2.png',
    'static/img/1.png',
    'static/img/my-icon-car.png'
];

// 检查文件大小并列出需要优化的文件
console.log('大文件分析：');
largeFiles.forEach(file => {
    if (fs.existsSync(file)) {
        const stats = fs.statSync(file);
        const sizeKB = (stats.size / 1024).toFixed(2);
        console.log(`${file}: ${sizeKB} KB`);

        if (sizeKB > 100) {
            console.log(`  ⚠️  该文件过大，建议优化`);
        }
    } else {
        console.log(`${file}: 文件不存在`);
    }
});

console.log('\n优化建议：');
console.log('1. 将 GIF 动画替换为更小的 Lottie 动画或使用 CSS 动画');
console.log('2. 压缩 PNG 图片，使用 TinyPNG 或类似工具');
console.log('3. 考虑使用 WebP 格式（微信小程序支持）');
console.log('4. 移除未使用的图片资源');

