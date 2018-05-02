webpackJsonp([1],{

/***/ "../../../../../src async recursive":
/***/ (function(module, exports) {

function webpackEmptyContext(req) {
	throw new Error("Cannot find module '" + req + "'.");
}
webpackEmptyContext.keys = function() { return []; };
webpackEmptyContext.resolve = webpackEmptyContext;
module.exports = webpackEmptyContext;
webpackEmptyContext.id = "../../../../../src async recursive";

/***/ }),

/***/ "../../../../../src/app/app.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, ".switch {\r\n  position: relative;\r\n  display: inline-block;\r\n  width: 60px;\r\n  height: 34px;\r\n}\r\n\r\n.switch input {display:none;}\r\n\r\n.slider {\r\n  position: absolute;\r\n  cursor: pointer;\r\n  top: 0;\r\n  left: 0;\r\n  right: 0;\r\n  bottom: 0;\r\n  background-color: #ccc;\r\n  transition: .4s;\r\n}\r\n\r\n.slider:before {\r\n  position: absolute;\r\n  content: \"\";\r\n  height: 26px;\r\n  width: 26px;\r\n  left: 4px;\r\n  bottom: 4px;\r\n  background-color: white;\r\n  transition: .4s;\r\n}\r\n\r\ninput:checked + .slider {\r\n  background-color: #2196F3;\r\n}\r\n\r\ninput:focus + .slider {\r\n  box-shadow: 0 0 1px #2196F3;\r\n}\r\n\r\ninput:checked + .slider:before {\r\n  -webkit-transform: translateX(26px);\r\n  transform: translateX(26px);\r\n}\r\n\r\n/* Rounded sliders */\r\n.slider.round {\r\n  border-radius: 34px;\r\n}\r\n\r\n.slider.round:before {\r\n  border-radius: 50%;\r\n}", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/app.component.html":
/***/ (function(module, exports) {

module.exports = "<nav *ngIf=\"!loggedIn\" class=\"navbar navbar-dark bg-primary\">\r\n  <a class=\"navbar-brand\" href=\"#\">IndCompli</a>\r\n</nav>\r\n<nav *ngIf=\"loggedIn\" class=\"navbar navbar-expand-lg navbar-dark bg-primary\">\r\n  <a class=\"navbar-brand\" href=\"#\">IndCompli</a>\r\n  <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n    <span class=\"navbar-toggler-icon\"></span>\r\n  </button>\r\n\r\n  <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n    <ul class=\"navbar-nav mr-auto\">\r\n      <li class=\"nav-item active\">\r\n        <a class=\"nav-link\" href=\"#\">My activity <span class=\"sr-only\">(current)</span></a>\r\n      </li>\r\n      <li class=\"nav-item\">\r\n        <a class=\"nav-link\" href=\"#\">License</a>\r\n      </li>      \r\n\t  <li class=\"nav-item\">\r\n        <a class=\"nav-link\" href=\"#\">Agreement</a>\r\n      </li>      \r\n\t  <li class=\"nav-item\">\r\n        <a class=\"nav-link\" href=\"#\">Repository</a>\r\n      </li>      \r\n\t  <li class=\"nav-item\">\r\n        <a class=\"nav-link\" href=\"#\">Reports</a>\r\n      </li>      \r\n    </ul>\r\n\t<form class=\"form-inline my-2 my-lg-0\">\r\n      <ul class=\"navbar-nav\">\r\n\t\t  <li class=\"nav-item dropdown\">\r\n\t\t\t<a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"navbarDropdown\" role=\"button\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\r\n\t\t\t  Welcome manager\r\n\t\t\t</a>\r\n\t\t\t<div class=\"dropdown-menu\" aria-labelledby=\"navbarDropdown\">\r\n\t\t\t  <a class=\"dropdown-item\" href=\"#\">Action</a>\r\n\t\t\t  <a class=\"dropdown-item\" href=\"#\">Another action</a>\r\n\t\t\t  <div class=\"dropdown-divider\"></div>\r\n\t\t\t  <a class=\"dropdown-item\" href=\"#\">Something else here</a>\r\n\t\t\t</div>\r\n\t\t  </li>\r\n\t  </ul>\r\n    </form>\r\n  </div>\r\n</nav>\r\n<div class=\"container-fluid\">\r\n\t\t<router-outlet></router-outlet>\r\n</div>"

/***/ }),

/***/ "../../../../../src/app/app.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__service_loginService__ = __webpack_require__("../../../../../src/app/service/loginService.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var AppComponent = (function () {
    function AppComponent(loginService) {
        this.loginService = loginService;
        this.title = 'app';
        this.loggedIn = this.loginService.isLoggedIn() ? true : false;
    }
    return AppComponent;
}());
AppComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_14" /* Component */])({
        selector: 'app-root',
        template: __webpack_require__("../../../../../src/app/app.component.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__service_loginService__["a" /* LoginService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__service_loginService__["a" /* LoginService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__service_loginService__["a" /* LoginService */]) === "function" && _a || Object])
], AppComponent);

var _a;
//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ "../../../../../src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__("../../../platform-browser/@angular/platform-browser.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__app_component__ = __webpack_require__("../../../../../src/app/app.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__login_app_login_login_component__ = __webpack_require__("../../../../../src/app/login/app.login.login.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__loggedin_app_logedin_component__ = __webpack_require__("../../../../../src/app/loggedin/app.logedin.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__login_app_login_forget_component__ = __webpack_require__("../../../../../src/app/login/app.login.forget.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__hardwareDetails_app_servercomhw_hardwaredetails_component__ = __webpack_require__("../../../../../src/app/hardwareDetails/app.servercomhw.hardwaredetails.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__hardwareSettings_app_servercomhw_hardwareSettings_component__ = __webpack_require__("../../../../../src/app/hardwareSettings/app.servercomhw.hardwareSettings.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__angular_forms__ = __webpack_require__("../../../forms/@angular/forms.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};











var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["b" /* NgModule */])({
        declarations: [
            __WEBPACK_IMPORTED_MODULE_3__app_component__["a" /* AppComponent */], __WEBPACK_IMPORTED_MODULE_4__login_app_login_login_component__["a" /* LoginLoginComponent */], __WEBPACK_IMPORTED_MODULE_7__hardwareDetails_app_servercomhw_hardwaredetails_component__["a" /* HardwareDetailsComponent */], __WEBPACK_IMPORTED_MODULE_8__hardwareSettings_app_servercomhw_hardwareSettings_component__["a" /* HardwareSettingsComponent */], __WEBPACK_IMPORTED_MODULE_6__login_app_login_forget_component__["a" /* LoginForgetComponent */], __WEBPACK_IMPORTED_MODULE_5__loggedin_app_logedin_component__["a" /* LogedInComponent */]
        ],
        imports: [
            __WEBPACK_IMPORTED_MODULE_10__angular_forms__["a" /* FormsModule */],
            __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
            __WEBPACK_IMPORTED_MODULE_2__angular_common_http__["a" /* HttpClientModule */],
            __WEBPACK_IMPORTED_MODULE_9__angular_router__["a" /* RouterModule */].forRoot([
                {
                    path: '',
                    component: __WEBPACK_IMPORTED_MODULE_4__login_app_login_login_component__["a" /* LoginLoginComponent */]
                },
                {
                    path: 'loggedin',
                    component: __WEBPACK_IMPORTED_MODULE_5__loggedin_app_logedin_component__["a" /* LogedInComponent */]
                },
                {
                    path: 'forget',
                    component: __WEBPACK_IMPORTED_MODULE_6__login_app_login_forget_component__["a" /* LoginForgetComponent */]
                },
                {
                    path: 'details/:id',
                    component: __WEBPACK_IMPORTED_MODULE_7__hardwareDetails_app_servercomhw_hardwaredetails_component__["a" /* HardwareDetailsComponent */]
                },
                {
                    path: 'settings/:id',
                    component: __WEBPACK_IMPORTED_MODULE_8__hardwareSettings_app_servercomhw_hardwareSettings_component__["a" /* HardwareSettingsComponent */]
                }
            ])
        ],
        providers: [],
        bootstrap: [__WEBPACK_IMPORTED_MODULE_3__app_component__["a" /* AppComponent */]]
    })
], AppModule);

//# sourceMappingURL=app.module.js.map

/***/ }),

/***/ "../../../../../src/app/hardwareDetails/app.servercomhw.hardwaredetails.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_switchMap__ = __webpack_require__("../../../../rxjs/add/operator/switchMap.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_switchMap___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_switchMap__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__ = __webpack_require__("../../../../../src/app/service/servercom.hardwareservice.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HardwareDetailsComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var HardwareDetailsComponent = (function () {
    function HardwareDetailsComponent(hardwareService, route, location) {
        this.hardwareService = hardwareService;
        this.route = route;
        this.location = location;
        this.rasPiDetails = {};
        this.switchNames = [];
        this.deviceId = 0;
        this.ishecked = 'checked';
    }
    HardwareDetailsComponent.prototype.onSwitchChange = function (e) {
        console.log("changed", e.target.checked, e.target.name, this.ishecked);
        this.hardwareService.toggleSwitch(this.deviceId, e.target.name, e.target.checked);
    };
    HardwareDetailsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.route.paramMap
            .switchMap(function (params) { return _this.hardwareService.getHardware(+params.get('id')); })
            .subscribe(function (rasPiDetails) {
            console.log(rasPiDetails);
            _this.switchNames = Object.keys(rasPiDetails);
            _this.rasPiDetails = rasPiDetails;
        });
        this.deviceId = +this.route.snapshot.paramMap.get('id');
    };
    return HardwareDetailsComponent;
}());
HardwareDetailsComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_14" /* Component */])({
        selector: 'hardware-details',
        template: __webpack_require__("../../../../../src/app/hardwareDetails/app.servercomhw.hardwaredetails.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__["a" /* HardwareService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__["a" /* HardwareService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__["a" /* HardwareService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* ActivatedRoute */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* ActivatedRoute */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_2__angular_common__["b" /* Location */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_common__["b" /* Location */]) === "function" && _c || Object])
], HardwareDetailsComponent);

var _a, _b, _c;
//# sourceMappingURL=app.servercomhw.hardwaredetails.component.js.map

/***/ }),

/***/ "../../../../../src/app/hardwareDetails/app.servercomhw.hardwaredetails.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"fluid-continer\">\r\n<div class=\"row\">\r\n\t<div class=\"col\">\r\n\t\t<div class=\"alert alert-primary\" role=\"alert\"><b>{{deviceId}}</b></div>\r\n\t\t\t<table class=\"table\">\r\n\t\t\t<thead>\r\n\t\t\t\t<tr><th>Switch</th><th>Status</th></tr>\r\n\t\t\t</thead>\r\n\t\t\t<tbody>\r\n\t\t\t<tr *ngFor=\"let switchName of switchNames\">\r\n\t\t\t  <td>{{switchName}}</td>\r\n\t\t\t  <td>\r\n\t\t\t\t<label class=\"switch\">\r\n\t\t\t\t\t<input type=\"checkbox\" name=\"{{switchName}}\" (change)=\"onSwitchChange($event)\" [checked]=\"rasPiDetails[switchName]=='ON'?'checked':''\"/>\r\n\t\t\t\t\t<span class=\"slider\"></span>\r\n\t\t\t\t</label>\t\r\n\t\t\t\t</td>\r\n\t\t\t</tr>\r\n\t\t\t</tbody>\r\n\t\t\t</table>\r\n\t</div>\r\n</div>\r\n</div>"

/***/ }),

/***/ "../../../../../src/app/hardwareSettings/app.servercomhw.hardwareSettings.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_switchMap__ = __webpack_require__("../../../../rxjs/add/operator/switchMap.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_switchMap___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_switchMap__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__ = __webpack_require__("../../../../../src/app/service/servercom.hardwareservice.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HardwareSettingsComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var HardwareSettingsComponent = (function () {
    function HardwareSettingsComponent(hardwareService, route, location) {
        this.hardwareService = hardwareService;
        this.route = route;
        this.location = location;
        this.rasPiDetails = {};
        this.switchNames = [];
        this.deviceId = 0;
        this.ishecked = 'checked';
    }
    HardwareSettingsComponent.prototype.onSwitchChange = function (e) {
        console.log("changed", e.target.checked, e.target.name, this.ishecked);
        this.hardwareService.toggleSwitch(this.deviceId, e.target.name, e.target.checked);
    };
    HardwareSettingsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.route.paramMap
            .switchMap(function (params) { return _this.hardwareService.getHardware(+params.get('id')); })
            .subscribe(function (rasPiDetails) {
            console.log(rasPiDetails);
            _this.switchNames = Object.keys(rasPiDetails);
            _this.rasPiDetails = rasPiDetails;
        });
        this.deviceId = +this.route.snapshot.paramMap.get('id');
    };
    return HardwareSettingsComponent;
}());
HardwareSettingsComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_14" /* Component */])({
        selector: 'hardware-details',
        template: __webpack_require__("../../../../../src/app/hardwareSettings/app.servercomhw.hardwareSettings.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__["a" /* HardwareService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__["a" /* HardwareService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__service_servercom_hardwareservice__["a" /* HardwareService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* ActivatedRoute */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* ActivatedRoute */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_2__angular_common__["b" /* Location */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_common__["b" /* Location */]) === "function" && _c || Object])
], HardwareSettingsComponent);

var _a, _b, _c;
//# sourceMappingURL=app.servercomhw.hardwareSettings.component.js.map

/***/ }),

/***/ "../../../../../src/app/hardwareSettings/app.servercomhw.hardwareSettings.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"fluid-continer\">\r\n<div class=\"row\">\r\n\t<div class=\"col\">\r\n\t\t<!--<div class=\"alert alert-primary\" role=\"alert\"><b>Setting </b>{{deviceId}}</div>-->\r\n\t\t<nav class=\"navbar navbar-expand-lg navbar-light\" style=\"background-color: #e3f2fd;\">\r\n\t\t\t<a class=\"navbar-brand\" ><b>Setting </b>{{deviceId}}</a>\r\n\t\t</nav>\t\r\n\t\t\t<form>\r\n\t\t\t  <div class=\"form-group\">\r\n\t\t\t\t<label for=\"exampleFormControlInput1\">Switch board name</label>\r\n\t\t\t\t<input type=\"text\" class=\"form-control\" id=\"exampleFormControlInput1\" placeholder=\"Hall\">\r\n\t\t\t  </div>\r\n\t\t\t  <div class=\"form-group\">\r\n\t\t\t\t<label for=\"exampleFormControlInput1\">Switch name</label>\r\n\t\t\t\t\t<table class=\"table\">\r\n\t\t\t\t\t<thead>\r\n\t\t\t\t\t\t<tr><th>Switch</th><th>Given name</th></tr>\r\n\t\t\t\t\t</thead>\r\n\t\t\t\t\t<tbody>\r\n\t\t\t\t\t<tr *ngFor=\"let switchName of switchNames\">\r\n\t\t\t\t\t  <td>{{switchName}}</td>\r\n\t\t\t\t\t  <td>\r\n\t\t\t\t\t\t\t<input type=\"text\" name=\"{{switchName}}\" class=\"form-control\"/>\r\n\t\t\t\t\t\t</td>\r\n\t\t\t\t\t</tr>\r\n\t\t\t\t\t</tbody>\r\n\t\t\t\t\t</table>\r\n\t\t\t  </div>\r\n\t\t\t  <div class=\"form-group\">\r\n\t\t\t\t<input type=\"button\" class=\"btn btn-success\" id=\"exampleFormControlInput1\" value=\"Save\">\r\n\t\t\t  </div>\r\n\t\t\t</form>\t\r\n\t</div>\r\n</div>\r\n</div>"

/***/ }),

/***/ "../../../../../src/app/loggedin/app.logedin.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__ = __webpack_require__("../../../../../src/app/service/servercom.hardwareservice.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LogedInComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var LogedInComponent = (function () {
    function LogedInComponent(hardwareService) {
        this.hardwareService = hardwareService;
        this.rasPiHardwares = [];
    }
    LogedInComponent.prototype.ngOnInit = function () {
        var that = this;
        /*setInterval(function(){
            that.getRasPiHardware();
        },1000);
        */
    };
    return LogedInComponent;
}());
LogedInComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_14" /* Component */])({
        selector: 'Login-Page',
        template: __webpack_require__("../../../../../src/app/loggedin/app.logedin.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__["a" /* HardwareService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__["a" /* HardwareService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__["a" /* HardwareService */]) === "function" && _a || Object])
], LogedInComponent);

var _a;
//# sourceMappingURL=app.logedin.component.js.map

/***/ }),

/***/ "../../../../../src/app/loggedin/app.logedin.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"container\">\r\n  Logged in\r\n</div>"

/***/ }),

/***/ "../../../../../src/app/login/app.login.forget.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__ = __webpack_require__("../../../../../src/app/service/servercom.hardwareservice.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LoginForgetComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var LoginForgetComponent = (function () {
    function LoginForgetComponent(hardwareService) {
        this.hardwareService = hardwareService;
        this.rasPiHardwares = [];
    }
    LoginForgetComponent.prototype.getRasPiHardware = function () {
        var _this = this;
        this.hardwareService.getAllHardwares().then(function (rasPiHardwares) { _this.rasPiHardwares = rasPiHardwares; });
    };
    LoginForgetComponent.prototype.ngOnInit = function () {
        var that = this;
        /*setInterval(function(){
            that.getRasPiHardware();
        },1000);
        */
    };
    return LoginForgetComponent;
}());
LoginForgetComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_14" /* Component */])({
        selector: 'Login-Page',
        template: __webpack_require__("../../../../../src/app/login/app.login.forget.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__["a" /* HardwareService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__["a" /* HardwareService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__service_servercom_hardwareservice__["a" /* HardwareService */]) === "function" && _a || Object])
], LoginForgetComponent);

var _a;
//# sourceMappingURL=app.login.forget.component.js.map

/***/ }),

/***/ "../../../../../src/app/login/app.login.forget.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"container\">\r\n  <div class=\"row\">\r\n    <div class=\"col-sm\">\r\n\t\t<br/>\r\n      <div class=\"card\" >\r\n\t\t  <img class=\"card-img-top\" src=\"assets/settings.png\" alt=\"indCompli\" height=\"200rem\" width=\"200rem\">\r\n\t\t  <div class=\"card-body\">\r\n\t\t\t<h4 class=\"card-title\">IndCompli</h4>\r\n\t\t\t<p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n\t\t\t\r\n\t\t  </div>\r\n\t\t</div>\r\n    </div>\r\n    <div class=\"col-sm\">\r\n\t\t<br/>\r\n\t\t<div class=\"card\">\r\n\t\t<div class=\"card-body\">\r\n\t\t<form>\r\n\t\t  <div class=\"form-group\">\r\n\t\t\t<label for=\"exampleInputEmail1\">Email address</label>\r\n\t\t\t<input type=\"email\" class=\"form-control\" id=\"exampleInputEmail1\" aria-describedby=\"emailHelp\" placeholder=\"Enter email\">\r\n\t\t\t<small id=\"emailHelp\" class=\"form-text text-muted\">We'll never share your email with anyone else.</small>\r\n\t\t  </div>\r\n\t\t  <input type=\"button\" class=\"btn btn-primary\" value=\"Request reset password\"/><a href=\"/\" class=\"btn btn-link\">Go to login</a>\r\n\t\t</form>\r\n\t\t</div>\r\n\t\t</div>\r\n\t\t<br/>\r\n\t\t<div class=\"card\">\r\n\t\t<div class=\"card-body\">\r\n\t\t\tNew to IndCompli ? <a href=\"#\">Register</a> or <a href=\"#\">Start trial</a>\r\n\t\t</div>\r\n\t\t</div>\r\n    </div>\r\n  </div>\r\n</div>"

/***/ }),

/***/ "../../../../../src/app/login/app.login.login.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__service_loginService__ = __webpack_require__("../../../../../src/app/service/loginService.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__typeBean_loginBean__ = __webpack_require__("../../../../../src/app/typeBean/loginBean.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LoginLoginComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var LoginLoginComponent = (function () {
    function LoginLoginComponent(loginService) {
        this.loginService = loginService;
        this.responseStr = {};
        this.username = "";
        this.password = "";
        this.hasError = false;
        this.loginBean = new __WEBPACK_IMPORTED_MODULE_2__typeBean_loginBean__["a" /* LoginBean */]();
    }
    LoginLoginComponent.prototype.handleError = function (error) {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    };
    LoginLoginComponent.prototype.login = function () {
        var _this = this;
        this.loginService.loginUserPost(this.loginBean.username, this.loginBean.password).then(function (responseStr) { _this.responseStr = responseStr; window.location.href = "loggedin"; })
            .catch(function (err) { _this.handleError(err); _this.hasError = true; });
    };
    LoginLoginComponent.prototype.hideError = function () {
        this.hasError = false;
    };
    LoginLoginComponent.prototype.ngOnInit = function () {
        var that = this;
        /*setInterval(function(){
            that.getRasPiHardware();
        },1000);
        */
        //this.login();
    };
    return LoginLoginComponent;
}());
LoginLoginComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_14" /* Component */])({
        selector: 'Login-Page',
        template: __webpack_require__("../../../../../src/app/login/app.login.login.html"),
        styles: [__webpack_require__("../../../../../src/app/app.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__service_loginService__["a" /* LoginService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__service_loginService__["a" /* LoginService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__service_loginService__["a" /* LoginService */]) === "function" && _a || Object])
], LoginLoginComponent);

var _a;
//# sourceMappingURL=app.login.login.component.js.map

/***/ }),

/***/ "../../../../../src/app/login/app.login.login.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"container\">\r\n  <div class=\"row\">\r\n    <div class=\"col-sm\">\r\n      <br/>\r\n      <div class=\"card\" >\r\n\t\t  <img class=\"card-img-top\" src=\"assets/settings.png\" alt=\"indCompli\" height=\"200rem\" width=\"200rem\">\r\n\t\t  <div class=\"card-body\">\r\n\t\t\t<h4 class=\"card-title\">IndCompli</h4>\r\n\t\t\t<p class=\"card-text\">Some quick example text to build on the card title and make up the bulk of the card's content.</p>\r\n\t\t\t\r\n\t\t  </div>\r\n\t\t</div>\r\n    </div>\r\n    <div class=\"col-sm\">\r\n\t\t<br/>\r\n\t\t<div class=\"card\">\r\n\t\t<div class=\"card-body\">\r\n\t\t<form>\r\n\t\t\r\n\t\t  <div *ngIf=\"hasError\" class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\r\n\t\t\t  <strong>Error!</strong> Invalid username or password\r\n\t\t\t  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" (click)=\"hideError()\">\r\n\t\t\t\t<span aria-hidden=\"true\">&times;</span>\r\n\t\t\t  </button>\r\n\t\t\t</div>\t\r\n\t\t  <div class=\"form-group\">\r\n\t\t\t<label for=\"exampleInputEmail1\">Email address</label>\r\n\t\t\t<input [(ngModel)]=\"loginBean.username\" type=\"email\" class=\"form-control\" id=\"exampleInputEmail1\" aria-describedby=\"emailHelp\" placeholder=\"Enter email\" name=\"username\">\r\n\t\t\t<small id=\"emailHelp\" class=\"form-text text-muted\">We'll never share your email with anyone else.</small>\r\n\t\t  </div>\r\n\t\t  <div class=\"form-group\">\r\n\t\t\t<label style=\"width:50%\" for=\"exampleInputPassword1\">Password</label>\r\n\t\t\t<label style=\"width:49%\"><a href=\"forget\">Forget password?</a></label>\r\n\t\t\t<input [(ngModel)]=\"loginBean.password\" type=\"password\" class=\"form-control\" id=\"exampleInputPassword1\" placeholder=\"Password\" name=\"password\">\r\n\t\t  </div>\r\n\t\t  <div class=\"form-check\">\r\n\t\t\t<label class=\"form-check-label\">\r\n\t\t\t  <input type=\"checkbox\" class=\"form-check-input\">\r\n\t\t\t  Remember me (for 30 days)\r\n\t\t\t</label>\r\n\t\t  </div>\r\n\t\t  <input type=\"button\" class=\"btn btn-primary\" value=\"Login\" (click)=\"login()\"/>\r\n\t\t</form>\r\n\t\t</div>\r\n\t\t</div>\r\n\t\t<br/>\r\n\t\t<div class=\"card\">\r\n\t\t<div class=\"card-body\">\r\n\t\t\tNew to IndCompli ? <a href=\"#\">Register</a> or <a href=\"#\">Start trial</a>\r\n\t\t</div>\r\n\t\t</div>\r\n    </div>\r\n  </div>\r\n</div>"

/***/ }),

/***/ "../../../../../src/app/service/loginService.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__ = __webpack_require__("../../../../rxjs/add/operator/toPromise.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LoginService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var LoginService = (function () {
    function LoginService(http) {
        this.http = http;
    }
    LoginService.prototype.handleError = function (error) {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    };
    LoginService.prototype.handleUserAction = function () {
        sessionStorage.setItem("loggedIn", "true");
    };
    LoginService.prototype.isLoggedIn = function () {
        return sessionStorage.getItem("loggedIn");
    };
    LoginService.prototype.test = function () {
        var url = "http://Vinayak-PC:8080/ComplianceTool/rest/user/test";
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    LoginService.prototype.loginUserPost = function (username, password) {
        var _this = this;
        var url = "/rest/user/login";
        return this.http.post(url, { username: username, password: password }).toPromise().then(function (response) { response; _this.handleUserAction(); }).catch(this.handleError);
    };
    LoginService.prototype.loginUser = function (username, password) {
        var url = "http://Vinayak-PC:8080/ComplianceTool/rest/user/login";
        return this.http.post(url, {}).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    LoginService.prototype.getAllHardwares = function () {
        var url = "http://Vinayak-PC:80/rest/raspi/all";
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    LoginService.prototype.getHardware = function (id) {
        var url = "http://Vinayak-PC:80/rest/raspi/device/" + id;
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    LoginService.prototype.toggleSwitch = function (deviceId, pinNo, pinStatus) {
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["b" /* HttpHeaders */]().set('Content-Type', 'application/json');
        console.log(headers);
        var url = "http://Vinayak-PC:80/rest/raspi/device/" + deviceId + "/" + pinNo + "/" + pinStatus;
        //let options = new RequestOptions({ headers: headers});
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    return LoginService;
}());
LoginService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["v" /* Injectable */])(),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpClient */]) === "function" && _a || Object])
], LoginService);

var _a;
//# sourceMappingURL=loginService.js.map

/***/ }),

/***/ "../../../../../src/app/service/servercom.hardwareservice.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__ = __webpack_require__("../../../../rxjs/add/operator/toPromise.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HardwareService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var HardwareService = (function () {
    function HardwareService(http) {
        this.http = http;
    }
    HardwareService.prototype.handleError = function (error) {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    };
    HardwareService.prototype.getAllHardwares = function () {
        var url = "http://Vinayak-PC:80/rest/raspi/all";
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    HardwareService.prototype.getHardware = function (id) {
        var url = "http://Vinayak-PC:80/rest/raspi/device/" + id;
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    HardwareService.prototype.toggleSwitch = function (deviceId, pinNo, pinStatus) {
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["b" /* HttpHeaders */]().set('Content-Type', 'application/json');
        console.log(headers);
        var url = "http://Vinayak-PC:80/rest/raspi/device/" + deviceId + "/" + pinNo + "/" + pinStatus;
        //let options = new RequestOptions({ headers: headers});
        return this.http.get(url).toPromise().then(function (response) { return response; }).catch(this.handleError);
    };
    return HardwareService;
}());
HardwareService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["v" /* Injectable */])(),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpClient */]) === "function" && _a || Object])
], HardwareService);

var _a;
//# sourceMappingURL=servercom.hardwareservice.js.map

/***/ }),

/***/ "../../../../../src/app/typeBean/loginBean.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LoginBean; });
var LoginBean = (function () {
    function LoginBean() {
    }
    return LoginBean;
}());

//# sourceMappingURL=loginBean.js.map

/***/ }),

/***/ "../../../../../src/environments/environment.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
// The file contents for the current environment will overwrite these during build.
var environment = {
    production: false
};
//# sourceMappingURL=environment.js.map

/***/ }),

/***/ "../../../../../src/main.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__("../../../platform-browser-dynamic/@angular/platform-browser-dynamic.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__("../../../../../src/app/app.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__("../../../../../src/environments/environment.ts");




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["a" /* enableProdMode */])();
}
__webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */]);
//# sourceMappingURL=main.js.map

/***/ }),

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("../../../../../src/main.ts");


/***/ })

},[0]);
//# sourceMappingURL=main.bundle.js.map