UrlParam = function () { // url参数
    let data, index;
    (function init() {
        data = [];    //值，如[["1","2"],["zhangsan"],["lisi"]]
        index = {};   //键:索引，如{a:0,b:1,c:2}
        let u = window.location.search.substr(1);
        if (u != '') {
            let params = decodeURIComponent(u).split('&');
            for (let i = 0, len = params.length; i < len; i++) {
                if (params[i] != '') {
                    let p = params[i].split("=");
                    if (p.length == 1 || (p.length == 2 && p[1] == '')) {// p | p= | =
                        data.push(['']);
                        index[p[0]] = data.length - 1;
                    } else if (typeof (p[0]) == 'undefined' || p[0] == '') { // =c 舍弃
                        continue;
                    } else if (typeof (index[p[0]]) == 'undefined') { // c=aaa
                        data.push([p[1]]);
                        index[p[0]] = data.length - 1;
                    } else {// c=aaa
                        data[index[p[0]]].push(p[1]);
                    }
                }
            }
        }
    })();
    return {
        // 获得参数,类似request.getParameter()
        param: function (o) { // o: 参数名或者参数次序
            try {
                return (typeof (o) == 'number' ? data[o][0] : data[index[o]][0]);
            } catch (e) {
            }
        },
        //获得参数组, 类似request.getParameterValues()
        paramValues: function (o) { //  o: 参数名或者参数次序
            try {
                return (typeof (o) == 'number' ? data[o] : data[index[o]]);
            } catch (e) {
            }
        },
        //是否含有paramName参数
        hasParam: function (paramName) {
            return typeof (paramName) == 'string' ? typeof (index[paramName]) != 'undefined' : false;
        },
        // 获得参数Map ,类似request.getParameterMap()
        paramMap: function () {
            let map = {};
            try {
                for (let p in index) {
                    map[p] = data[index[p]];
                }
            } catch (e) {
            }
            return map;
        }
    }
}();

function initIndex() {
    $.get('/article/list', function (result) {
        $.each(result.data, function (index, article) {
            $("#article-list").append(
                "<li class='list-group-item d-flex justify-content-between align-items-center'>"
                + "<a href=\"article.html?uid=" + article.id + "\" target='_blank'>" + article.title + "</a>"
                + "<span class='badge badge-light'>" + article.createDate + "</span>"
                + "</li>"
            );
        });
    });

    //加载网站信息
    $.get("/getSiteInfo", function (res) {
        if (res && res.data) {
            $("#domain").text(res.data.domain);
        }
    });

    getLoginInfo();
}

function loadArticle() {
    let articleId = UrlParam.param('uid');
    console.log(articleId);
    let toolbar_default = ["bold", "italic", "strikethrough", "heading", "code", "quote", "unordered-list", "ordered-list", "clean-block", "link", "image", "table", "horizontal-rule", "preview", "side-by-side", "fullscreen"];
    let toolbar_create = {
        name: "save",
        action: function save() {
            window.location.replace("/article.html");
        },
        className: "fa fa-pencil-square-o",
        title: "Create",
    };
    let toolbar_save = {
        name: "save",
        action: function save(editor) {
            $.post('/article/save',
                {
                    id: $("#articleId").val(),
                    content: editor.value()
                },
                function (res) {
                    console.log(res);
                    alert(res.message);
                });
        },
        className: "fa fa-save",
        title: "Save",
    };
    if (articleId) {
        $("#articleId").val(articleId);
        $.get('/article/' + articleId, function (res) {
            let article = res.data;
            let simplemde = new SimpleMDE({
                element: document.getElementById("mde"),
                spellCheck: false,
                initialValue: article.content,
                toolbar: toolbar_default.concat(toolbar_save),
                renderingConfig: {
                    codeSyntaxHighlighting: true
                }
            });
            if (!simplemde.isPreviewActive()) {
                window.onload = autoClick;
            }
        });
    } else {
        let simplemde = new SimpleMDE({
            element: document.getElementById("mde"),
            spellCheck: false,
            initialValue: "# 请开始你的表演 \n---\n",
            toolbar: toolbar_default.concat(toolbar_save),
            renderingConfig: {
                codeSyntaxHighlighting: true
            }
        });
    }
    //加载网站信息
    $.get("/getSiteInfo", function (res) {
        let site = res.data;
        console.log(site);
        $("#domain").text(res.data.domain);
        $("#motto").text(res.data.motto);
    });

    getLoginInfo();
}

function login() {
    console.log(window.location.href);
    $.get("/oauth/github/url", {state: window.location.href}, function (res) {
        window.location.replace(res.data);
    });
}

function search() {
    var a = $("#key").val();
    var content = $("#content");
    content.empty();

    $.get("/search", {keyword: a, page: 0, size: 20}, function (res) {
        let data = res.body.data.content;
        for (i = 0; i < data.length; i++) {
            content.append("<div class='title'>" + data[i].title + "</div>");
            let b = "<div>" + data[i].content + "</div>";
            content.append(b);
        }
        console.log(res);
    })
}

//点击preview快捷键
function autoClick() {
    $(".fa-eye").trigger("click");
}

//加载登录信息
function getLoginInfo() {
    $.get("/getUser", function (res) {
        if (res.code === "200") {
            let user = res.data;
            console.log(user);
            $("#avatar").attr({src: user.avatar_url});
            $("#create").removeAttr("hidden");
            let login = $("#login");
            if (login) {
                login.attr({href: user.html_url, target: "_blank"});
                login.text('  ' + user.login);
                login.removeAttr("onclick");
            }
        } else {
            console.log(res.data);
        }
    });
}

function getServerInfo() {
    $.get("/getServerInfo", function (res) {
        console.log("server info: " + res.data);
        if (res.code === "200") {
            let serverInfo = res.data;
            console.log(serverInfo);

            let chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'chart'
                },
                title: {
                    text: '版本分布分析'
                },
                plotArea: {
                    shadow: null,
                    borderWidth: null,
                    backgroundColor: null
                },
                tooltip: {
                    formatter: function () {
                        return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.percentage, 1) + '% (' +
                            Highcharts.numberFormat(this.y, 0, ',') + ' 个)';
                    }
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            formatter: function () {
                                if (this.percentage > 4) return this.point.name;
                            },
                            color: 'white',
                            style: {
                                font: '13px Trebuchet MS, Verdana, sans-serif'
                            }
                        }
                    }
                },
                legend: {
                    backgroundColor: '#FFFFFF',
                    x: 0,
                    y: -30
                },
                credits: {
                    enabled: false
                },
                series: [{
                    type: 'pie',
                    name: 'Browser share',
                    data: [
                        ['1.1', 3617],
                        ['1.2.1', 3436],
                        ['1.0', 416],
                        ['1.3', 2],
                        ['1.2', 1],
                        ['新增对比', 5000]
                    ]
                }]
            });

        } else {
            console.log(res.data);
        }
    });
}