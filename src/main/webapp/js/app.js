/* 陪玩订单系统前端交互：首页轮播 + 大厅筛选排序（原生 JS，无依赖） */
(function () {
    'use strict';

    /* ---------------- 首页轮播 ---------------- */
    var slides = Array.prototype.slice.call(document.querySelectorAll('.banner .slide'));
    var dots = Array.prototype.slice.call(document.querySelectorAll('.banner .dots i'));

    if (slides.length > 0) {
        var current = 0;
        var timer = null;

        var show = function (index) {
            slides.forEach(function (s, i) { s.classList.toggle('active', i === index); });
            dots.forEach(function (d, i) { d.classList.toggle('active', i === index); });
            current = index;
        };
        var start = function () {
            timer = setInterval(function () { show((current + 1) % slides.length); }, 4500);
        };
        var stop = function () {
            if (timer) { clearInterval(timer); timer = null; }
        };

        dots.forEach(function (dot, i) {
            dot.addEventListener('click', function () { stop(); show(i); start(); });
        });
        // 鼠标悬停暂停，移出继续
        var banner = document.querySelector('.banner');
        banner.addEventListener('mouseenter', stop);
        banner.addEventListener('mouseleave', start);

        show(0);
        start();
    }

    /* ---------------- 大厅筛选 / 排序 ---------------- */
    var grid = document.getElementById('mateGrid');
    if (!grid) {
        return;
    }

    var cards = Array.prototype.slice.call(grid.querySelectorAll('.mate-card'));
    var chips = Array.prototype.slice.call(document.querySelectorAll('.chip'));
    var sortBtns = Array.prototype.slice.call(document.querySelectorAll('.sort-btn'));
    var emptyTip = document.getElementById('emptyTip');

    cards.forEach(function (card, i) { card.dataset.idx = i; });

    /* 按游戏名筛选 */
    var filter = function (keyword) {
        var visible = 0;
        cards.forEach(function (card) {
            var game = card.getAttribute('data-game') || '';
            var matched = keyword === '全部' || game.indexOf(keyword) > -1;
            card.style.display = matched ? '' : 'none';
            if (matched) { visible++; }
        });
        if (emptyTip) { emptyTip.style.display = visible === 0 ? 'block' : 'none'; }
    };

    /* 排序：默认 / 价格从低到高 / 评分从高到低 */
    var sortBy = function (mode) {
        var list = cards.slice();
        if (mode === 'price') {
            list.sort(function (a, b) { return parseFloat(a.dataset.price) - parseFloat(b.dataset.price); });
        } else if (mode === 'score') {
            list.sort(function (a, b) { return parseFloat(b.dataset.score) - parseFloat(a.dataset.score); });
        } else {
            list.sort(function (a, b) { return a.dataset.idx - b.dataset.idx; });
        }
        list.forEach(function (card) { grid.appendChild(card); });
    };

    chips.forEach(function (chip) {
        chip.addEventListener('click', function () {
            chips.forEach(function (c) { c.classList.remove('active'); });
            chip.classList.add('active');
            filter(chip.getAttribute('data-filter'));
        });
    });

    sortBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            sortBtns.forEach(function (b) { b.classList.remove('active'); });
            btn.classList.add('active');
            sortBy(btn.getAttribute('data-sort'));
        });
    });
})();
