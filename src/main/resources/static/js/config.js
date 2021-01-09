
(function($) {

	config.getUrl = function(name) {
		if(!config.URL[name]) {
			return "";
		}
		return config.domain + config.URL[name];
	};

	/**
	 * 获取或设置本地存储
	 * */
	config.storage = function(key, value) {
		var f = mui.os.plus ? plus.storage : localStorage;
		if(arguments.length == 2) { //设置
			return f.setItem(key, value);
		} else {
			return f.getItem(key) == null ? "" : f.getItem(key);
		}
	};

	//调试记录
	config.debug = function() {
		config.debug && console.log.apply(window.console, arguments);
	};

	/**
	 * 获取当前状态
	 **/
	config.getState = function() {
		var stateText = localStorage.getItem('$state') || "{}";
		return JSON.parse(stateText);
	};
	/**
	 * 
	 * 封装的ajax
	 */
	config.getAjaxByParm = function(params) {
		params = params || {};
		var urlName = params.url || '';
		var data = params.data || {};
		var success = params.success || function() {};
		var error = params.error || '';
		var options = params.options || {};
		var complete = params.complete || function() {};
		var ajaxType = params.ajaxType || 'post';
		var url = config.getUrl(urlName);
		if(!url) {
			console.log(urlName + " get url is null");
			return;
		}
		var token = config.storage('openid');
		
		data.openid = token;
		
		if(params.submitBtn) {
			if(params.submitBtn.getAttribute('disabled')){
        		return;
        	}
			params.submitBtn.setAttribute('disabled', true);
		}
		mui.ajax(url, {
			data: data,
			dataType: 'json', //服务器返回json格式数据
			type: ajaxType, //HTTP请求类型
			timeout: 10000, //超时时间设置为10秒；
			beforeSend: function(xhr) {
				xhr.timeout = options && options.timeout || 10000; //默认10秒超时
				xhr.onabort = function() { //mui.ajax没有封装此事件，故手动添加下
					mui.toast("请求被中止");
				}
			},
			success: function(data) {
				data.status = Number(data.status);
				if(data.status < 1025) {
					if(data.status == 0 || data.status == 3) {
						success(data);
					} else {
						data.bizError = true;
						success(data);
					}
				} else if(data.status == 1027) {
					mui.alert('温馨提示：' + data.msg, '教师助手', function() {
						window.location = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + config.helperTeacherAppId + '&redirect_uri=http%3A%2F%2F' + config.wxDomainName + '%2Fhelper%2Fteacher%2Fmenu%2FtoSchoolClass.htm%3Fschoolid%3D' + config.schoolId + '&response_type=code&scope=snsapi_base&state=wx#wechat_redirect';
					});
				} else {
					mui.toast(data.msg);
				}
				if(params.submitBtn) {
					params.submitBtn.removeAttribute('disabled');
				}
			},
			error: function(xhr, xhrErrMsg) {
				if(params.submitBtn) {
					params.submitBtn.removeAttribute('disabled');
				}
				if(xhr.status == 400) { //400状态，是服务端自定义的错误状态，提供了友好的错误信息
					try {
						var resObj = JSON.parse(xhr.response);
						if(resObj && resObj.message) {
							mui.toast(resObj.message);
						}
					} catch(e) {}
				} else if(xhr.status == 401) { //服务端定义的Session失效
					mui.toast('您未登录或者登录已失效');
				} else if(navigator.onLine == false) {
					mui.toast("您的网络已断开")
				} else if(xhrErrMsg == 'timeout') {
					mui.toast("请求已超时"); //可能原因分三类：（1）客户端网络状况不好没发出去，（2）服务器端繁忙没有响应，（3）网络中途任意环节超时
				} else if(xhrErrMsg == 'abort') {
					mui.toast('请求被终止,可能是遇到不安全的请求，比如跨域');
				} else {
					if(!window.plus) {
						if(error) {
							error();
						} else {
							mui.toast(xhrErrMsg);
						}
						return;
					}
					var nt = plus.networkinfo;
					var s = nt.getCurrentType();
					if(s == nt.CONNECTION_NONE || s == nt.CONNECTION_UNKNOW) {
						mui.toast("您的网络已断开");
						return;
					}
					mui.toast(xhrErrMsg);
				}
			},
			complete: complete
		});

	};
	config.downByUrl = function(urlPath) {
		var dtask = plus.downloader.createDownload(urlPath, {
			filename: "_doc/myPic.png"
		}, function(d, status) {
			// 下载完成
			if(status == 200) {
				config.debug("Download success: " + d.filename);
			} else {
				config.debug("Download failed: " + status);
			}
		});
		dtask.start();
	};
	/**
	 * 获取url中的参数
	 */
	config.GetQueryString = function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg); 
		if(r != null) return decodeURI(r[2]);
		return null;
	}

	/**
	 * 获取url中的参数(页面间传值用到，不用再解码)
	 */
	config.GetUrlString = function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null) return decodeURI(unescape(r[2]));
		return null;
	}

	/**
	 * 编码Url中的参数（页面间传值用到）
	 * */
	config.GetEncodeString = function(name) {
		var urlString = encodeURI(encodeURI(name));
		if(urlString != null) return urlString;
		return null;
	}

	/**
	 * URL的参数转换成json对象
	 * */
	config.parseQueryString = function(url) {
		url = decodeURI(decodeURI(url));
		var reg_url = /^[^\?]+\?([\w\W]+)$/,
			reg_para = /([^&=]+)=([\w\W]*?)(&|$|#)/g,
			arr_url = reg_url.exec(url),
			ret = {};
		if(arr_url && arr_url[1]) {
			var str_para = arr_url[1],
				result;
			while((result = reg_para.exec(str_para)) != null) {
				ret[result[1]] = result[2];
			}
		}
		return ret;
	}

	/**
	 * 通用空的模板
	 * */
	config.emptyTpl = [
		'<div class="mui-table-view-cell emptyTpl">',
		'  <h4 class="mui-ellipsis tc">',
		'    <p class="mui-h5">',
		'     ${msg}',
		'    </p>',
		'  </h4>',
		'</div>'
	].join('');

	/*
	 * 填充空的模板
	 */
	config.setEmpty = function(box, data) {
		$('.mui-pull-top-pocket')[0].classList.remove('mui-visibility');
		box.innerHTML = juicer(config.emptyTpl, data);
		//box.classList.add('bg_transparent');
	}

	/**
	 * 验证是否是手机号码
	 * */
	config.isPhone = function(phoneStr) {
		//验证手机号码
		if(!/^1(3|5|7|8|4)\d{9}$/.test(phoneStr)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否是验证码
	 * */
	config.isCode = function(Str) {

		if(!/^\d{4}$/.test(Str)) {
			return false;
		}
		return true;
	}

	/**
	 * 设置title值
	 * */
	config.setTitle = function(Str) {
		$('.mui-title')[0].innerHTML = Str;
		window.document.title = Str;
		if(!mui.os.android) {
			// hack在微信等webview中无法修改document.title的情况
			var iframe = '<iframe src="../favicon.ico" id="fixtitle" style="display:none;"></iframe>';
			$('body')[0].insertAdjacentHTML("beforeEnd", iframe);
			var $iframe = $('#fixtitle');
			$iframe.on('load', function() {
				setTimeout(function() {
					$iframe.off('load').remove();

				}, 0);
			});
		}
	}

	/*
	 * 模糊搜索
	 */
	config.searchIndexedList = {
		init: function(box) {
			var self = {};
			self.box = box;
			self.inner = self.box.querySelector('ul.indexed-list-inner');
			self.liArray = [].slice.call(self.inner.querySelectorAll('li.indexed-list-item'));
			self.alert = self.box.querySelector('.indexed-list-alert');
			self.styleForSearch = document.createElement('style');
			self.wrapper = self.box.querySelector('.mui-scroll-wrapper');
			(document.head || document.body).appendChild(self.styleForSearch);
			return self;
		},
		find: function(self, keyword) {
			keyword = (keyword || '').toLowerCase();
			var selectorBuffer = [];
			var groupIndex = -1;
			var itemCount = 0;
			var liArray = self.liArray;
			var itemTotal = liArray.length;
			liArray.forEach(function(item) {
				var currentIndex = liArray.indexOf(item);
				var text = (item.innerText || '').toLowerCase();
				var label = (item.getAttribute('data-label') || '').toLowerCase().split(',');

				var tags = (item.getAttribute('data-tags') || '').toLowerCase();
				if(keyword && (label[0].indexOf(keyword) >= 0 || label[1].indexOf(keyword) >= 0 || label[2].indexOf(keyword) >= 0)) {
					selectorBuffer.push('li.indexed-list-item' + ':nth-child(' + (currentIndex + 1) + ')');
					itemCount++;
				}
				if(selectorBuffer.length >= itemTotal) {
					self.box.classList.remove('empty');
					self.box.classList.add('height');
					self.styleForSearch.innerText = "";
				} else if(selectorBuffer.length > 0) {
					self.box.classList.remove('empty');
					self.box.classList.add('height');
					self.styleForSearch.innerText = selectorBuffer.join(', ') + "{display:block;}";
				} else {
					self.box.classList.add('empty');
					self.box.classList.remove('height');

				}
			});
		}
	}

	/*
	 * 通用事件
	 */
	config.initEvent = function() {

		//li跳转页面
		mui('.mui-table-view').on('tap', 'li[data-href]', function(event) {
			var url = this.getAttribute('data-href');
			$.openWindow({
				url: url,
				id: url,
				preload: true,
				show: {
					aniShow: 'pop-in'
				},
				styles: {
					popGesture: 'hide'
				},
				waiting: {
					autoShow: false
				}
			});
		}, false);
		//button跳转页面
		mui('body').on('tap', 'button[data-href]', function(event) {
			var url = this.getAttribute('data-href');
			$.openWindow({
				url: '../' + url,
				id: url,
				preload: true,
				show: {
					aniShow: 'pop-in'
				},
				styles: {
					popGesture: 'hide'
				},
				waiting: {
					autoShow: false
				}
			});
		}, false);
		//绝对路径跳转
		mui('body').on('tap', 'button[data-httpHref]', function(event) {
			var url = this.getAttribute('data-httpHref');
			$.openWindow({
				url: url,
				id: url,
				preload: true,
				show: {
					aniShow: 'pop-in'
				},
				styles: {
					popGesture: 'hide'
				},
				waiting: {
					autoShow: false
				}
			});
		}, false);

	}
	config.imageLazyload = function(callback) {
		callback = callback || $.noop;
		//图片懒加载
		$(document).imageLazyload({
			placeholder: '../img/60x60.gif',
			callbacks: callback()
		});
	}
	config.initEvent();

	"function" == typeof define && define.amd && define("config", [], function() {
		return config
	})
}(mui));
(function($, window) {
	var CLASS_POPUP = 'mui-popup';
	var CLASS_POPUP_BACKDROP = 'mui-popup-backdrop';
	var CLASS_POPUP_IN = 'mui-popup-in';
	var CLASS_POPUP_OUT = 'mui-popup-out';
	var CLASS_POPUP_INNER = 'mui-popup-inner';
	var CLASS_POPUP_TITLE = 'mui-popup-title';
	var CLASS_POPUP_TEXT = 'mui-popup-text';
	var CLASS_POPUP_INPUT = 'mui-popup-input';
	var CLASS_POPUP_BUTTONS = 'mui-popup-buttons';
	var CLASS_POPUP_BUTTON = 'mui-popup-button';
	var CLASS_POPUP_BUTTON_BOLD = 'mui-popup-button-bold';
	var CLASS_POPUP_BACKDROP = 'mui-popup-backdrop';
	var CLASS_ACTIVE = 'mui-active';
	var CLASS_POPUP_BUTTON_CANCEL = 'mui-popup-cancel';
	var popupStack = [];
	var backdrop = (function() {
		var element = document.createElement('div');
		element.classList.add(CLASS_POPUP_BACKDROP);
		element.addEventListener('mousemove', function(e) {
			e.preventDefault();
		});
		element.addEventListener('webkitTransitionEnd', function() {
			if(!this.classList.contains(CLASS_ACTIVE)) {
				element.parentNode && element.parentNode.removeChild(element);
			}
		});
		return element;
	}());
	config.backdrop = {
		show: function() {
			var CLASS_ACTIVE = "mui-active";
			if(!backdrop.classList.contains(CLASS_ACTIVE)) {
				backdrop.style.display = 'block';
				document.body.appendChild(backdrop);
				backdrop.offsetHeight;
				backdrop.classList.add(CLASS_ACTIVE);
			}
		},
		hide: function() {
			var CLASS_ACTIVE = "mui-active";
			backdrop.classList.remove(CLASS_ACTIVE);
		}
	}

	/**
	 * 通用弹窗
	 */
	config.dialog = {
		show: function(html, callback, clickcallback) {
			var popupElement = document.createElement('div');
			popupElement.className = CLASS_POPUP;
			popupElement.classList.add('top30');
			popupElement.innerHTML = html;

			var removePopupElement = function() {
				popupElement.parentNode && popupElement.parentNode.removeChild(popupElement);
				popupElement = null;
			};
			config.backdrop.show();
			popupElement.addEventListener($.EVENT_MOVE, $.preventDefault);
			popupElement.addEventListener('webkitTransitionEnd', function(e) {
				if(popupElement && e.target === popupElement && popupElement.classList.contains(CLASS_POPUP_OUT)) {
					removePopupElement();
				}
			});

			popupElement.style.display = 'block';
			document.body.appendChild(popupElement);
			popupElement.offsetHeight;
			popupElement.classList.add(CLASS_POPUP_IN);
			callback && callback();
			var btns = $.qsa('.' + CLASS_POPUP_BUTTON, popupElement);
			var input = popupElement.querySelector('.' + CLASS_POPUP_INPUT + ' input');
			var popup = {
				element: popupElement,
				close: function(index, animate) {
					if(popupElement) {
						clickcallback && clickcallback({
							index: index || 0,
							value: input && input.value || ''
						});
						if(animate !== false) {
							popupElement.classList.remove(CLASS_POPUP_IN);
							popupElement.classList.add(CLASS_POPUP_OUT);
						} else {
							removePopupElement();
						}
						popupStack.pop();
						//如果还有其他popup，则不remove backdrop
						if(popupStack.length) {
							popupStack[popupStack.length - 1]['show'](animate);
						} else {
							backdrop.classList.remove(CLASS_ACTIVE);
						}
					}
				}
			};
			var handleEvent = function(e) {
				popup.close(btns.indexOf(e.target));
			};
			$(popupElement).on('tap', '.' + CLASS_POPUP_BUTTON_CANCEL, handleEvent);
			if(popupStack.length) {
				popupStack[popupStack.length - 1]['hide']();
			}
			popupStack.push({
				close: popup.close,
				show: function(animate) {
					popupElement.style.display = 'block';
					popupElement.offsetHeight;
					popupElement.classList.add(CLASS_POPUP_IN);
				},
				hide: function() {
					popupElement.style.display = 'none';
					popupElement.classList.remove(CLASS_POPUP_IN);
				}
			});

		},
		hide: function(popupElement) {
			$.trigger(popupElement.querySelector('.' + CLASS_POPUP_BUTTON_CANCEL), 'tap');
		}

	};

}(mui));

(function($, window) {
	//字符串format方法
	String.prototype.format = function() {
		var o = arguments;
		if(o.length == 0) return this.toString();
		o = o.length == 1 && typeof o[0] == "object" ? o[0] : o;
		return this.replace(/{([^{}]+)}/g, function($0, $1, index, self) {
			return o[$1] != undefined && o[$1] != null ? o[$1] : "";
		});
	};
	String.prototype.trim = function() {　　
			return this.replace(/(^\s*)|(\s*$)/g, "");　　
		}
		/**
		 * 添加url参数
		 * @param {String} name
		 * @param {String} value
		 */
	String.prototype.addUrlPara = function(name, value) {
		var currentUrl = this.split('#')[0];
		if(/\?/g.test(currentUrl)) {
			/*if(/name=[-\w]{4,25}/g.test(currentUrl)) {
				currentUrl = currentUrl.replace(/name=[-\w]{4,25}/g, name + "=" + value);
			} else {
				currentUrl += "&" + name + "=" + value;
			} */
			currentUrl += "&" + name + "=" + value;
		} else {
			currentUrl += "?" + name + "=" + value;
		}
		if(this.split('#')[1]) {
			return currentUrl + '#' + this.split('#')[1];
		} else {
			return currentUrl;
		}
	}
	String.prototype.addUrlParas = function(params) {

	}

	/*日期格式化*/
	Date.prototype.format = function(format) {
		var o = {
			"M+": this.getMonth() + 1, //month
			"d+": this.getDate(), //day
			"h+": this.getHours(), //hour
			"m+": this.getMinutes(), //minute
			"s+": this.getSeconds(), //second
			"q+": Math.floor((this.getMonth() + 3) / 3), //quarter
			"S": this.getMilliseconds() //millisecond
		}
		if(/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		}

		for(var k in o) {
			if(new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	};

	"function" == typeof define && define.amd && define("common", [], function() {
		return c
	})
	$.fn.is = function matchesSelector(selector) {
		var element = this[0];
		if(element.matches) {
			return element.matches(selector);
		} else if(element.matchesSelector) {
			return element.matchesSelector(selector);
		} else if(element.webkitMatchesSelector) {
			return element.webkitMatchesSelector(selector);
		} else if(element.msMatchesSelector) {
			return element.msMatchesSelector(selector);
		} else if(element.mozMatchesSelector) {
			return element.mozMatchesSelector(selector);
		} else if(element.oMatchesSelector) {
			return element.oMatchesSelector(selector);
		} else if(element.querySelectorAll) {
			var matches = (element.document || element.ownerDocument).querySelectorAll(selector),
				i = 0;

			while(matches[i] && matches[i] !== element) i++;
			return matches[i] ? true : false;
		}
		throw new Error('Your browser version is too old,please upgrade your browser');
	}

}(mui, window));