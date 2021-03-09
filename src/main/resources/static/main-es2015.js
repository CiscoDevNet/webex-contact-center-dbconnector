(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["main"],{

/***/ "./$$_lazy_route_resource lazy recursive":
/*!******************************************************!*\
  !*** ./$$_lazy_route_resource lazy namespace object ***!
  \******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncaught exception popping up in devtools
	return Promise.resolve().then(function() {
		var e = new Error("Cannot find module '" + req + "'");
		e.code = 'MODULE_NOT_FOUND';
		throw e;
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "./$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/about/about.component.html":
/*!**********************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/about/about.component.html ***!
  \**********************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>about works!</p>\n");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/app.component.html":
/*!**************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/app.component.html ***!
  \**************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<!-- <app-endpoint></app-endpoint> -->\n<!-- <<app-connector></app-connector> -->\n\n<nav class=\"header\" id=\"styleguideheader\" role=\"navigation\">\n    <div class=\"container-fluid\">\n        <div class=\"header-panels\">\n            <div class=\"header-panel hidden-md-down\">\n                <a class=\"header__logo\" href=\"http://www.cisco.com\" target=\"_blank\">\n                    <span class=\"icon-cisco\"></span>\n                </a>\n                <h1 class=\"header__title\">\n                    <span routerLink=\"health\">dbConnector</span>\n                </h1>\n            </div>\n            <div class=\"header-panel header-panel--center base-margin-left base-margin-right hidden-lg-up\">\n                <a class=\"header__logo\" href=\"http://www.cisco.com\" target=\"_blank\">\n                    <span class=\"icon-cisco\"></span>\n                </a>\n            </div>\n            <div class=\"header-panel header-panel--right hidden-md-down\">\n                \n                <a *ngIf=\"isLoggedIn\" routerLink=\"connector\" class=\"header-item\">Connector</a>\n                <a *ngIf=\"isLoggedIn\" routerLink=\"endpoint\" class=\"header-item\">Endpoints</a>\n                <a *ngIf=\"isLoggedIn\" routerLink=\"GridView\" class=\"header-item\">Grid View</a>\n                <a routerLink=\"help\" class=\"header-item\">Help</a>\n                <a routerLink=\"support\" class=\"header-item\">Support</a>\n                <a routerLink=\"about\" class=\"header-item\">About</a>\n                <a *ngIf=\"isLoggedIn\" routerLink=\"logout\" class=\"header-item\">Logout</a>\n                <a *ngIf=\"!isLoggedIn\" (click)=\"loginButton($event)\" class=\"header-item\">Login</a>\n            </div>\n        </div>\n    </div>\n</nav>\n<router-outlet></router-outlet>");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/connector/connector.component.html":
/*!******************************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/connector/connector.component.html ***!
  \******************************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n\n<div *ngIf=\"isLoggedIn\" class=\"container\">\n\t<div *ngIf=\"isworking\" class=\"loader\" aria-label=\"Loading, please wait...\"\n\t\tstyle=\"position: absolute;  width: 70px;  height: 70px;  z-index: 15;  top: 50%;  left: 50%;  margin: -100px 0 0 -150px;\">\n\t\t<div class=\"wrapper\">\n\t\t\t<div class=\"wheel\"></div>\n\t\t</div>\n\t</div>\n\t<div class=\"row\">\n\t\t<div class=\"col\">\n\t\t\t<div class=\"panel panel--loose panel--raised base-margin-bottom\">\n\t\t\t\t<h2 class=\"subtitle\">Connector</h2>\n\t\t\t\t<hr>\n\t\t\t\t<div class=\"form-group base-margin-bottom\">\n\t\t\t\t\t<div class=\"form-group__text select\">\n\t\t\t\t\t\t<select (change)=\"onOptionsSelected($event)\" [(ngModel)]=\"connector.type\" id=\"input-type-select\">\n\t\t\t\t\t\t\t<option value=\"MySQL\">MySQL</option>\n\t\t\t\t\t\t\t<option value=\"SQL_Server\">SQL Server</option>\n\t\t\t\t\t\t</select>\n\t\t\t\t\t\t<!-- <label for=\"input-type-select\">Connector</label> -->\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t\t<!--  -->\n\t\t\t\t<hr>\n\t\t\t\t<div class=\"section\">\n\t\t\t\t\t<ul id=\"embossed\" class=\"tabs tabs--embossed\">\n\t\t\t\t\t\t<li (click)=\"showServerTabButton('Server')\" id=\"embossed-1\" class=\"tab \">\n\t\t\t\t\t\t\t<a tabindex=\"0\">Server</a>\n\t\t\t\t\t\t</li>\n\t\t\t\t\t\t<li (click)=\"showServerTabButton('ConnectionPool')\" id=\"embossed-2\" class=\"tab\">\n\t\t\t\t\t\t\t<a tabindex=\"0\">Connection Pool</a>\n\t\t\t\t\t\t</li>\n\t\t\t\t\t</ul>\n\t\t\t\t\t<div id=\"embossed-content\" class=\"tab-content\">\n\t\t\t\t\t\t<div *ngIf=\"showTab1\" id=\"embossed-1-content\" class=\"tab-pane active\">\n\t\t\t\t\t\t\t<div class=\"panel panel--loose panel--raised base-margin-bottom\">\n\t\t\t\t\t\t\t\t<h2 class=\"subtitle\">Connector</h2>\n\t\t\t\t\t\t\t\t<hr>\n\t\t\t\t\t\t\t\t<form name=\"form\">\n\n\t\t\t\t\t\t\t\t\t<div class=\"form-group base-margin-bottom\">\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Host\" (change)=\"updateConnectionString()\"\n\t\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.hostname\" [ngModelOptions]=\"{standalone: true}\"\n\t\t\t\t\t\t\t\t\t\t\t\trequired type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Host\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.hostname.length < 1}\">Host</label>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-DatabaseName\" (change)=\"updateConnectionString()\"\n\t\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.database\" [ngModelOptions]=\"{standalone: true}\"\n\t\t\t\t\t\t\t\t\t\t\t\ttype=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-DatabaseName\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.database.length < 1}\">Database\n\t\t\t\t\t\t\t\t\t\t\t\tName</label>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Port\" (change)=\"updateConnectionString()\"\n\t\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.port\" [ngModelOptions]=\"{standalone: true}\"\n\t\t\t\t\t\t\t\t\t\t\t\trequired type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Port\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.port < 1}\">Port</label>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Username\" (change)=\"updateConnectionString()\"\n\t\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.username\" [ngModelOptions]=\"{standalone: true}\"\n\t\t\t\t\t\t\t\t\t\t\t\trequired type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Username\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.username < 1}\">Username</label>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Password\" (change)=\"updateConnectionString()\"\n\t\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.password\" [ngModelOptions]=\"{standalone: true}\"\n\t\t\t\t\t\t\t\t\t\t\t\trequired type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Password\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.password < 1}\">Password</label>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-ConnectionString\"\n\t\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionString\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-ConnectionString\"\n\t\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionString < 1}\">Connection\n\t\t\t\t\t\t\t\t\t\t\t\tString</label>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t\t<button (click)=\"saveConnection()\" class=\"btn btn--ghost\" [disabled]=\"isworking\">Test\n\t\t\t\t\t\t\t\t\t\t\t\tConnection</button>\n\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</form>\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t\t<div *ngIf=\"showTab2\" id=\"embossed-2-content\" class=\"tab-pane active\">\n\t\t\t\t\t\t\t<div class=\"panel panel--loose panel--raised base-margin-bottom\">\n\t\t\t\t\t\t\t\t<h2 class=\"subtitle\">Connection Pool</h2>\n\t\t\t\t\t\t\t\t<hr>\n\t\t\t\t\t\t\t\t<!--  -->\n\n\t\t\t\t\t\t\t\t<div class=\"form-group base-margin-bottom\">\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-InitialPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionPool.initialPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-InitialPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionPool.initialPoolSize < 1}\">Initial\n\t\t\t\t\t\t\t\t\t\t\tPool Size</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-MaxStatements\"\n\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionPool.maxStatements\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-MaxStatements\"\n\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionPool.maxStatements < 1}\">Max\n\t\t\t\t\t\t\t\t\t\t\tStatements</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-MinPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionPool.minPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-MinPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionPool.minPoolSize < 1}\">Min\n\t\t\t\t\t\t\t\t\t\t\tPool Size</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-MaxPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionPool.maxPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-MaxPoolSize\"\n\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionPool.maxPoolSize < 1}\">Max\n\t\t\t\t\t\t\t\t\t\t\tPool Size</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-AcquireIncrement\"\n\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionPool.acquireIncrement\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-AcquireIncrement\"\n\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionPool.acquireIncrement < 1}\">Acquire\n\t\t\t\t\t\t\t\t\t\t\tIncrement</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-ConnectionTimeout\"\n\t\t\t\t\t\t\t\t\t\t\t[(ngModel)]=\"connector.connectionPool.unreturnedConnectionTimeout\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" required type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-ConnectionTimeout\"\n\t\t\t\t\t\t\t\t\t\t\t[ngClass]=\"{'text-danger': connector.connectionPool.unreturnedConnectionTimeout < 1}\">Connection\n\t\t\t\t\t\t\t\t\t\t\tTimeout</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<button (click)=\"saveConnection()\" class=\"btn btn--ghost\">Save Connection\n\t\t\t\t\t\t\t\t\t\t\tPool</button>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</div>\n\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t\t<div class=\"col\">\n\t\t\t<label for=\"textarea-state-default\">Console</label>\n\t\t\t<div class=\"form-group__text\">\n\t\t\t\t<textarea id=\"textarea-state-default\" class=\"text-white\"\n\t\t\t\t\tstyle=\"background-color: #000000;-webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box; width: 100%;\"\n\t\t\t\t\trows=\"41\">{{myConsole}}</textarea>\n\t\t\t</div>\n\t\t</div>\n\n\t</div>\n</div>");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/endpoint/endpoint.component.html":
/*!****************************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/endpoint/endpoint.component.html ***!
  \****************************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n\n<div *ngIf=\"isLoggedIn\" class=\"container\">\n\t<div *ngIf=\"isworking\" class=\"loader\" aria-label=\"Loading, please wait...\"\n\t\tstyle=\"position: absolute;  width: 70px;  height: 70px;  z-index: 15;  top: 50%;  left: 50%;  margin: -100px 0 0 -150px;\">\n\t\t<div class=\"wrapper\">\n\t\t\t<div class=\"wheel\"></div>\n\t\t</div>\n\t</div>\n\t<div class=\"row\">\n\t\t<div   *ngIf=\"endpoints.length > 0\"   class=\"col\">\n\t\t\t<div class=\"panel panel--loose panel--raised base-margin-bottom\">\n\t\t\t\t<h2 class=\"subtitle\">Endpoints</h2>\n\t\t\t\t<hr>\n\t\t\t\t<div class=\"section\">\n\t\t\t\t\t<div class=\"form-group base-margin-bottom\">\n\n\t\t\t\t\t\t<div class=\"form-group__check\">\n\t\t\t\t\t\t\t<input type=\"checkbox\" class=\"form-check-input\" id=\"BasicAuthenticationisRequired\"\n\t\t\t\t\t\t\t\t[checked]=\"basicAuth.isBasicAuthenticationRequired\"\n\t\t\t\t\t\t\t\t[(ngModel)]=\"basicAuth.isBasicAuthenticationRequired\" \n\t\t\t\t\t\t\t\t(change)=\"setBasicAuthenticationRequired($event);showServerTabButton('tab1'); \"\n\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\">\n\t\t\t\t\t\t\t<label class=\"form-check-label\" for=\"BasicAuthenticationisRequired\">Basic Authentication is\n\t\t\t\t\t\t\t\tRequired</label>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t\t<ul id=\"embossed\" class=\"tabs tabs--embossed\">\n\t\t\t\t\t\t<li (click)=\"showServerTabButton('tab1')\" id=\"embossed-1\" class=\"tab \">\n\t\t\t\t\t\t\t<a tabindex=\"0\">Endpoint</a>\n\t\t\t\t\t\t</li>\n\t\t\t\t\t\t<li *ngIf=\"basicAuth.isBasicAuthenticationRequired\" (click)=\"showServerTabButton('tab2')\" id=\"embossed-2\" class=\"tab\">\n\t\t\t\t\t\t\t<a tabindex=\"0\">Authentication</a>\n\t\t\t\t\t\t</li>\n\t\t\t\t\t</ul>\n\t\t\t\t\t<div id=\"embossed-content\" class=\"tab-content\">\n\t\t\t\t\t\t<div *ngIf=\"showTab1\" id=\"embossed-1-content\" class=\"tab-pane active\">\n\t\t\t\t\t\t\t<div class=\"panel panel--loose panel--raised base-margin-bottom\">\n\t\t\t\t\t\t\t\t<!-- <h2 class=\"subtitle\">Endpoint</h2> -->\n\t\t\t\t\t\t\t\t<!-- <hr> -->\n\t\t\t\t\t\t\t\t<div class=\"form-group base-margin-bottom\">\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-endpoint\" [(ngModel)]=\"endpoint.endpoint\" readonly\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-endpoint\">endpoint</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Description\" [(ngModel)]=\"endpoint.description\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\"\n\t\t\t\t\t\t\t\t\t\t\taria-placeholder=\"Enter description\"\n\t\t\t\t\t\t\t\t\t\t\tplaceholder=\"Enter description\"\n\t\t\t\t\t\t\t\t\t\t\t>\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Description\">Description</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\n\t\t\t\t\t\t\t\t\t\t<div class=\"container\">\n\t\t\t\t\t\t\t\t\t\t\t<!-- <div class=\"row\">\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-12 text-center\">\n\t\t\t\t\t\t\t\t\t\t\t\t\t<hr>\n\t\t\t\t\t\t\t\t\t\t\t\t\t<h2 class=\"subtitle\">Name Value List</h2>\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t</div> -->\n\t\t\t\t\t\t\t\t\t\t\t<div class=\"row\">\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-1\">\n\t\t\t\t\t\t\t\t\t\t\t\t\t<button (click)=\"addNameValuePair({})\"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"btn btn--link \">[+]</button>\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-4 text-center\">\n\t\t\t\t\t\t\t\t\t\t\t\t\tName\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-2 text-center\">\n\t\t\t\t\t\t\t\t\t\t\t\t\tparameters\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-4 text-center\">\n\t\t\t\t\t\t\t\t\t\t\t\t\tValue\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t<div class=\"row\">\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-12\">\n\t\t\t\t\t\t\t\t\t\t\t\t\t<hr>\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t</div>\n\n\t\t\t\t\t\t\t\t\t\t\t<div class=\"row\"\n\t\t\t\t\t\t\t\t\t\t\t\t*ngFor=\"let nameValue of endpoint.nameValueList; index as i;\">\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-1\">\n\t\t\t\t\t\t\t\t\t\t\t\t\t<button (click)=\"deleteNameValuePair(i)\"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"btn btn--link btn-sm\">[-]</button>\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-5\">\n\t\t\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-NamePair_name{{i}}\" [(ngModel)]=\"nameValue.name\"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"col-5\">\n\t\t\t\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-ValuePair_value{{i}}\" [(ngModel)]=\"nameValue.value\"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\">\n\t\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t\t</div>\n\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Query\" [(ngModel)]=\"endpoint.query\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Query\">Query</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Username\">Console</label>\n\t\t\t\t\t\t\t\t\t\t<textarea id=\"textarea-state-default\" class=\"text-white\" readonly\n\t\t\t\t\t\t\t\t\t\t\tstyle=\"background-color: #000000;width: 100%;\"\n\t\t\t\t\t\t\t\t\t\t\trows=\"8\">{{myConsole}}</textarea>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<p></p>\n\t\t\t\t\t\t\t\t\t\t<button (click)=\"saveEndpointAndTest()\" class=\"btn btn--ghost\" [disabled]=\"isworking\" >Save and Test\n\t\t\t\t\t\t\t\t\t\t\tEndpoint</button>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</div>\n\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t\t<div *ngIf=\"showTab2\" id=\"embossed-2-content\" class=\"tab-pane active\">\n\t\t\t\t\t\t\t<div class=\"panel panel--loose panel--raised base-margin-bottom\">\n\t\t\t\t\t\t\t\t<h2 class=\"subtitle\">Basic Authentication for Endpoints</h2>\n\t\t\t\t\t\t\t\tApplies to all endpoints\n\t\t\t\t\t\t\t\t<hr>\n\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t<div class=\"form-group base-margin-bottom\">\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Username\" placeholder=\"must be atleast 12 charactors\"\n\t\t\t\t\t\t\t\t\t\t\t(change)=\"updateBasicAuthValue()\" [(ngModel)]=\"basicAuth.username\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Username\">Username</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Password\" placeholder=\"must be atleast 12 charactors\"\n\t\t\t\t\t\t\t\t\t\t\t(change)=\"updateBasicAuthValue()\" [(ngModel)]=\"basicAuth.password\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\">\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Password\">Password</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<input id=\"input-type-Base64Value\" placeholder=\"{{base64ValuePlaceholder}}\"\n\t\t\t\t\t\t\t\t\t\t\t(change)=\"updateBasicAuthValue()\" [(ngModel)]=\"basicAuth.value\"\n\t\t\t\t\t\t\t\t\t\t\t[ngModelOptions]=\"{standalone: true}\" type=\"text\" readonly>\n\t\t\t\t\t\t\t\t\t\t<label for=\"input-type-Base64Value\">Base64 Value</label>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t\t<!--  -->\n\t\t\t\t\t\t\t\t\t<div class=\"form-group__text\">\n\t\t\t\t\t\t\t\t\t\t<p></p>\n\t\t\t\t\t\t\t\t\t\t<button (click)=\"saveBasicAuthentication()\" class=\"btn btn--ghost\">Save Basic\n\t\t\t\t\t\t\t\t\t\t\tAuthentication</button>\n\t\t\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t\t\t</div>\n\n\t\t\t\t\t\t\t</div>\n\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t\t<div class=\"col\">\n\t\t\t<div class=\"row\">\n\t\t\t\t<div class=\"col\">\n\t\t\t\t\t<h4>Current Endpoints</h4>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t\t<div class=\"row\">\n\t\t\t\t<div class=\"col\">\n\t\t\t\t\t<label><button (click)=\"addEndpoint()\" class=\"btn btn--ghost\">add</button></label>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t\t<div class=\"row\">\n\t\t\t\t<div class=\"col\">\n\t\t\t\t\t<!-- space -->\n\t\t\t\t\t<hr>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t\t<div class=\"row\">\n\t\t\t\t<div class=\"col\">\n\t\t\t\t\t<div \n\t\t\t\t\t[ngClass]=\"{'bg-primary': endpoint.isHighlighted === true}\"\n\t\t\t\t\t*ngFor=\"let endpoint of endpoints; index as i;\" (click)=\"selectEndpoint(endpoint)\">\n\t\t\t\t\t\t<div (click)=\"deleteEndpoint(i)\" class=\"alert__icon icon-error-outline\"> </div>\n\t\t\t\t\t\t{{endpoint.endpoint}}<br>\n\t\t\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{endpoint.description}}\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</div>");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/grid-view/grid-view.component.html":
/*!******************************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/grid-view/grid-view.component.html ***!
  \******************************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n<div class=\"container\">\n\n\n  <div class=\"row\">\n    <div class=\"col-sm\">\n    </div>\n    <div class=\"col-sm\">\n      <div class=\"input-group mb-3\">\n        <div class=\"input-group-prepend\">\n          <label class=\"input-group-text\" for=\"inputGroupSelect01\">Select endPoint</label>\n        </div>\n        <!-- <select  (change)=\"executeEndPoint(endpoint)\" class=\"custom-select\" id=\"inputGroupSelect01\">\n          <option >Choose...</option>\n          <option *ngFor=\"let endpoint of endpoints; index as i;\" [value]=\"endpoint\">{{endpoint.description}}</option>\n\n        </select> -->\n\n        <select (change)=\"executeEndPoint($event)\" [(ngModel)]=\"selectedEndPoint\" id=\"input-type-select\">\n          <option value=\"Choose\">Choose...</option>\n          <option *ngFor=\"let endpoint of endpoints; index as i;\" value=\"{{endpoint.name}}\">{{endpoint.description}}\n          </option>\n        </select>\n\n      </div>\n    </div>\n    <div class=\"col-sm\">\n    </div>\n  </div>\n  <p>&nbsp;</p>\n  <div class=\"row text-left\">\n    <div class=\"col\">&nbsp;</div>\n    <div class=\"col\">\n      <table *ngIf=\"headers.length > 0\" class=\"table table--lined table--selectable\" aria-label=\"Selectable rows table example\">\n        <thead>\n          <tr>\n            <th scope=\"col\">#</th>\n            <th *ngFor=\"let header of headers;\" scope=\"col\">{{header}}</th>\n          </tr>\n        </thead>\n        <tbody>\n          <tr *ngFor=\"let record of records; index as i;\">\n            <th scope=\"row\">{{(i + 1)}}</th>\n          <td *ngFor=\"let column of record; index as i2;\">{{column}} </td>\n          </tr>\n\n        </tbody>\n      </table>\n    </div>\n    <div class=\"col\">&nbsp;</div>\n  </div>\n</div>");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/help/help.component.html":
/*!********************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/help/help.component.html ***!
  \********************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p (click)=\"clickMe()\">help works!</p>\n");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/loggedin/loggedin.component.html":
/*!****************************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/loggedin/loggedin.component.html ***!
  \****************************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>loggedin works!</p>\n");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/login/login.component.html":
/*!**********************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/login/login.component.html ***!
  \**********************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n\n<section class=\"h-100\">\n\t<header class=\"container h-100\">\n\t\t<div class=\"d-flex align-items-center justify-content-center h-100\">\n\t\t\t<div class=\"d-flex flex-column\">\n\t\t\t\t<div class=\"text-center\">\n\t\t\t\t\t<h2 class=\"subtitle\"></h2>\n\t\t\t\t\t<div class=\"section\">\n\t\t\t\t\t\t<button class=\"btn btn--ghost\" (click)=\"clickMe($event)\">Login</button>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t</header>\n</section>\n");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/logout/logout.component.html":
/*!************************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/logout/logout.component.html ***!
  \************************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n\n<section *ngIf=\"message.length == 0\" class=\"text-center\">\n\t<form>\n\t\t<h2 class=\"form-signin-heading\">Are you sure you want to log out?</h2>\n\t\t<input name=\"_csrf\" type=\"hidden\" value=\"${_csrf.token}\">\n\t\t<button (click)=\"myLogout()\" class=\"btn btn-lg btn-primary btn-block\" type=\"button\">Log Out</button>\n\t</form>\n</section>\n<section *ngIf=\"message.length > 0\" class=\"text-center\">\n\t{{message}}\n</section>\n<router-outlet></router-outlet>\n");

/***/ }),

/***/ "./node_modules/raw-loader/dist/cjs.js!./src/app/support/support.component.html":
/*!**************************************************************************************!*\
  !*** ./node_modules/raw-loader/dist/cjs.js!./src/app/support/support.component.html ***!
  \**************************************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>support works!</p>\n");

/***/ }),

/***/ "./node_modules/tslib/tslib.es6.js":
/*!*****************************************!*\
  !*** ./node_modules/tslib/tslib.es6.js ***!
  \*****************************************/
/*! exports provided: __extends, __assign, __rest, __decorate, __param, __metadata, __awaiter, __generator, __createBinding, __exportStar, __values, __read, __spread, __spreadArrays, __await, __asyncGenerator, __asyncDelegator, __asyncValues, __makeTemplateObject, __importStar, __importDefault, __classPrivateFieldGet, __classPrivateFieldSet */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__extends", function() { return __extends; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__assign", function() { return __assign; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__rest", function() { return __rest; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__decorate", function() { return __decorate; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__param", function() { return __param; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__metadata", function() { return __metadata; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__awaiter", function() { return __awaiter; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__generator", function() { return __generator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__createBinding", function() { return __createBinding; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__exportStar", function() { return __exportStar; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__values", function() { return __values; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__read", function() { return __read; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__spread", function() { return __spread; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__spreadArrays", function() { return __spreadArrays; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__await", function() { return __await; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__asyncGenerator", function() { return __asyncGenerator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__asyncDelegator", function() { return __asyncDelegator; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__asyncValues", function() { return __asyncValues; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__makeTemplateObject", function() { return __makeTemplateObject; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__importStar", function() { return __importStar; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__importDefault", function() { return __importDefault; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__classPrivateFieldGet", function() { return __classPrivateFieldGet; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "__classPrivateFieldSet", function() { return __classPrivateFieldSet; });
/*! *****************************************************************************
Copyright (c) Microsoft Corporation.

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
***************************************************************************** */
/* global Reflect, Promise */

var extendStatics = function(d, b) {
    extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return extendStatics(d, b);
};

function __extends(d, b) {
    extendStatics(d, b);
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
}

var __assign = function() {
    __assign = Object.assign || function __assign(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p)) t[p] = s[p];
        }
        return t;
    }
    return __assign.apply(this, arguments);
}

function __rest(s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
}

function __decorate(decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
}

function __param(paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
}

function __metadata(metadataKey, metadataValue) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(metadataKey, metadataValue);
}

function __awaiter(thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
}

function __generator(thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
}

function __createBinding(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}

function __exportStar(m, exports) {
    for (var p in m) if (p !== "default" && !exports.hasOwnProperty(p)) exports[p] = m[p];
}

function __values(o) {
    var s = typeof Symbol === "function" && Symbol.iterator, m = s && o[s], i = 0;
    if (m) return m.call(o);
    if (o && typeof o.length === "number") return {
        next: function () {
            if (o && i >= o.length) o = void 0;
            return { value: o && o[i++], done: !o };
        }
    };
    throw new TypeError(s ? "Object is not iterable." : "Symbol.iterator is not defined.");
}

function __read(o, n) {
    var m = typeof Symbol === "function" && o[Symbol.iterator];
    if (!m) return o;
    var i = m.call(o), r, ar = [], e;
    try {
        while ((n === void 0 || n-- > 0) && !(r = i.next()).done) ar.push(r.value);
    }
    catch (error) { e = { error: error }; }
    finally {
        try {
            if (r && !r.done && (m = i["return"])) m.call(i);
        }
        finally { if (e) throw e.error; }
    }
    return ar;
}

function __spread() {
    for (var ar = [], i = 0; i < arguments.length; i++)
        ar = ar.concat(__read(arguments[i]));
    return ar;
}

function __spreadArrays() {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};

function __await(v) {
    return this instanceof __await ? (this.v = v, this) : new __await(v);
}

function __asyncGenerator(thisArg, _arguments, generator) {
    if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
    var g = generator.apply(thisArg, _arguments || []), i, q = [];
    return i = {}, verb("next"), verb("throw"), verb("return"), i[Symbol.asyncIterator] = function () { return this; }, i;
    function verb(n) { if (g[n]) i[n] = function (v) { return new Promise(function (a, b) { q.push([n, v, a, b]) > 1 || resume(n, v); }); }; }
    function resume(n, v) { try { step(g[n](v)); } catch (e) { settle(q[0][3], e); } }
    function step(r) { r.value instanceof __await ? Promise.resolve(r.value.v).then(fulfill, reject) : settle(q[0][2], r); }
    function fulfill(value) { resume("next", value); }
    function reject(value) { resume("throw", value); }
    function settle(f, v) { if (f(v), q.shift(), q.length) resume(q[0][0], q[0][1]); }
}

function __asyncDelegator(o) {
    var i, p;
    return i = {}, verb("next"), verb("throw", function (e) { throw e; }), verb("return"), i[Symbol.iterator] = function () { return this; }, i;
    function verb(n, f) { i[n] = o[n] ? function (v) { return (p = !p) ? { value: __await(o[n](v)), done: n === "return" } : f ? f(v) : v; } : f; }
}

function __asyncValues(o) {
    if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
    var m = o[Symbol.asyncIterator], i;
    return m ? m.call(o) : (o = typeof __values === "function" ? __values(o) : o[Symbol.iterator](), i = {}, verb("next"), verb("throw"), verb("return"), i[Symbol.asyncIterator] = function () { return this; }, i);
    function verb(n) { i[n] = o[n] && function (v) { return new Promise(function (resolve, reject) { v = o[n](v), settle(resolve, reject, v.done, v.value); }); }; }
    function settle(resolve, reject, d, v) { Promise.resolve(v).then(function(v) { resolve({ value: v, done: d }); }, reject); }
}

function __makeTemplateObject(cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};

function __importStar(mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (Object.hasOwnProperty.call(mod, k)) result[k] = mod[k];
    result.default = mod;
    return result;
}

function __importDefault(mod) {
    return (mod && mod.__esModule) ? mod : { default: mod };
}

function __classPrivateFieldGet(receiver, privateMap) {
    if (!privateMap.has(receiver)) {
        throw new TypeError("attempted to get private field on non-instance");
    }
    return privateMap.get(receiver);
}

function __classPrivateFieldSet(receiver, privateMap, value) {
    if (!privateMap.has(receiver)) {
        throw new TypeError("attempted to set private field on non-instance");
    }
    privateMap.set(receiver, value);
    return value;
}


/***/ }),

/***/ "./src/app/about/about.component.css":
/*!*******************************************!*\
  !*** ./src/app/about/about.component.css ***!
  \*******************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2Fib3V0L2Fib3V0LmNvbXBvbmVudC5jc3MifQ== */");

/***/ }),

/***/ "./src/app/about/about.component.ts":
/*!******************************************!*\
  !*** ./src/app/about/about.component.ts ***!
  \******************************************/
/*! exports provided: AboutComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AboutComponent", function() { return AboutComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");



let AboutComponent = class AboutComponent {
    constructor() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        console.log('AboutComponent: ngOnInit', this.isLoggedIn);
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
};
AboutComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-about',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./about.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/about/about.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./about.component.css */ "./src/app/about/about.component.css")).default]
    })
], AboutComponent);



/***/ }),

/***/ "./src/app/app.component.css":
/*!***********************************!*\
  !*** ./src/app/app.component.css ***!
  \***********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2FwcC5jb21wb25lbnQuY3NzIn0= */");

/***/ }),

/***/ "./src/app/app.component.ts":
/*!**********************************!*\
  !*** ./src/app/app.component.ts ***!
  \**********************************/
/*! exports provided: AppComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppComponent", function() { return AppComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _restservice_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./restservice.service */ "./src/app/restservice.service.ts");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./global-constants */ "./src/app/global-constants.ts");




let AppComponent = class AppComponent {
    constructor(restService) {
        this.restService = restService;
        this.title = 'dbConnector';
        this.isLoggedIn = false;
        this.restService.onLoginChange.subscribe({
            next: (event) => {
                console.log('Received message  ', event);
                this.isLoggedIn = event;
                _global_constants__WEBPACK_IMPORTED_MODULE_3__["GlobalConstants"].isLoggedIn = this.isLoggedIn;
            }
        });
    }
    ngOnChanges(changes) {
        console.log('AppComponent: ngOnChanges', changes);
    }
    ngOnInit() {
        console.log('AppComponent: ngOnInit');
        this.restService.isAuthenticated();
    }
    loginButton() {
        console.log('AppComponent: loginButton');
        if (this.isLoggedIn) {
            // life is good
        }
        else {
            location.assign('/oauth2/authorization/dbconnector');
        }
    }
};
AppComponent.ctorParameters = () => [
    { type: _restservice_service__WEBPACK_IMPORTED_MODULE_2__["RestserviceService"] }
];
AppComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-root',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./app.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/app.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./app.component.css */ "./src/app/app.component.css")).default]
    })
], AppComponent);



/***/ }),

/***/ "./src/app/app.module.ts":
/*!*******************************!*\
  !*** ./src/app/app.module.ts ***!
  \*******************************/
/*! exports provided: AppModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppModule", function() { return AppModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser */ "./node_modules/@angular/platform-browser/fesm2015/platform-browser.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm2015/router.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm2015/http.js");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @angular/forms */ "./node_modules/@angular/forms/fesm2015/forms.js");
/* harmony import */ var ngx_cookie_service__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ngx-cookie-service */ "./node_modules/ngx-cookie-service/fesm2015/ngx-cookie-service.js");
/* harmony import */ var _app_component__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./app.component */ "./src/app/app.component.ts");
/* harmony import */ var _connector_connector_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./connector/connector.component */ "./src/app/connector/connector.component.ts");
/* harmony import */ var _endpoint_endpoint_component__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ./endpoint/endpoint.component */ "./src/app/endpoint/endpoint.component.ts");
/* harmony import */ var _logout_logout_component__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! ./logout/logout.component */ "./src/app/logout/logout.component.ts");
/* harmony import */ var _help_help_component__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! ./help/help.component */ "./src/app/help/help.component.ts");
/* harmony import */ var _support_support_component__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! ./support/support.component */ "./src/app/support/support.component.ts");
/* harmony import */ var _about_about_component__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! ./about/about.component */ "./src/app/about/about.component.ts");
/* harmony import */ var _login_login_component__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(/*! ./login/login.component */ "./src/app/login/login.component.ts");
/* harmony import */ var _loggedin_loggedin_component__WEBPACK_IMPORTED_MODULE_15__ = __webpack_require__(/*! ./loggedin/loggedin.component */ "./src/app/loggedin/loggedin.component.ts");
/* harmony import */ var _grid_view_grid_view_component__WEBPACK_IMPORTED_MODULE_16__ = __webpack_require__(/*! ./grid-view/grid-view.component */ "./src/app/grid-view/grid-view.component.ts");


















// import { GlobalConstants } from './global-constants';
let AppModule = class AppModule {
};
AppModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_2__["NgModule"])({
        declarations: [
            // GlobalConstants,
            _app_component__WEBPACK_IMPORTED_MODULE_7__["AppComponent"],
            _connector_connector_component__WEBPACK_IMPORTED_MODULE_8__["ConnectorComponent"],
            _endpoint_endpoint_component__WEBPACK_IMPORTED_MODULE_9__["EndpointComponent"],
            _logout_logout_component__WEBPACK_IMPORTED_MODULE_10__["LogoutComponent"],
            _help_help_component__WEBPACK_IMPORTED_MODULE_11__["HelpComponent"],
            _support_support_component__WEBPACK_IMPORTED_MODULE_12__["SupportComponent"],
            _about_about_component__WEBPACK_IMPORTED_MODULE_13__["AboutComponent"],
            _login_login_component__WEBPACK_IMPORTED_MODULE_14__["LoginComponent"],
            _loggedin_loggedin_component__WEBPACK_IMPORTED_MODULE_15__["LoggedinComponent"],
            _grid_view_grid_view_component__WEBPACK_IMPORTED_MODULE_16__["GridViewComponent"],
        ],
        imports: [
            _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__["BrowserModule"],
            _angular_common_http__WEBPACK_IMPORTED_MODULE_4__["HttpClientModule"],
            _angular_forms__WEBPACK_IMPORTED_MODULE_5__["FormsModule"],
            _angular_forms__WEBPACK_IMPORTED_MODULE_5__["ReactiveFormsModule"],
            _angular_router__WEBPACK_IMPORTED_MODULE_3__["RouterModule"].forRoot([
                { path: '', component: _app_component__WEBPACK_IMPORTED_MODULE_7__["AppComponent"] },
                { path: 'logout', component: _logout_logout_component__WEBPACK_IMPORTED_MODULE_10__["LogoutComponent"] },
                { path: 'help', component: _help_help_component__WEBPACK_IMPORTED_MODULE_11__["HelpComponent"] },
                { path: 'support', component: _support_support_component__WEBPACK_IMPORTED_MODULE_12__["SupportComponent"] },
                { path: 'about', component: _about_about_component__WEBPACK_IMPORTED_MODULE_13__["AboutComponent"] },
                { path: 'login', component: _login_login_component__WEBPACK_IMPORTED_MODULE_14__["LoginComponent"] },
                { path: 'connector', component: _connector_connector_component__WEBPACK_IMPORTED_MODULE_8__["ConnectorComponent"] },
                { path: 'endpoint', component: _endpoint_endpoint_component__WEBPACK_IMPORTED_MODULE_9__["EndpointComponent"] },
                { path: 'loggedin', component: _loggedin_loggedin_component__WEBPACK_IMPORTED_MODULE_15__["LoggedinComponent"] },
                { path: 'GridView', component: _grid_view_grid_view_component__WEBPACK_IMPORTED_MODULE_16__["GridViewComponent"] },
            ]),
        ],
        exports: [_angular_router__WEBPACK_IMPORTED_MODULE_3__["RouterModule"]],
        providers: [ngx_cookie_service__WEBPACK_IMPORTED_MODULE_6__["CookieService"]],
        bootstrap: [_app_component__WEBPACK_IMPORTED_MODULE_7__["AppComponent"]]
    })
], AppModule);



/***/ }),

/***/ "./src/app/connector/connector.component.css":
/*!***************************************************!*\
  !*** ./src/app/connector/connector.component.css ***!
  \***************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2Nvbm5lY3Rvci9jb25uZWN0b3IuY29tcG9uZW50LmNzcyJ9 */");

/***/ }),

/***/ "./src/app/connector/connector.component.ts":
/*!**************************************************!*\
  !*** ./src/app/connector/connector.component.ts ***!
  \**************************************************/
/*! exports provided: ConnectorComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ConnectorComponent", function() { return ConnectorComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _restservice_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../restservice.service */ "./src/app/restservice.service.ts");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");




let ConnectorComponent = class ConnectorComponent {
    constructor(restService) {
        this.restService = restService;
        this.isworking = true;
        this.showTab1 = true;
        this.connector = {
            type: '',
            version: '',
            hostname: '',
            port: '',
            database: '',
            username: '',
            password: '',
            driver: '',
            connectionString: '',
            connectionPool: {
                initialPoolSize: '',
                minPoolSize: '',
                acquireIncrement: '',
                maxPoolSize: '',
                maxStatements: '',
                unreturnedConnectionTimeout: ''
            }
        };
        this.myConsole = '';
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_3__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        console.log('ConnectorComponent: ngOnInit', this.isLoggedIn);
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_3__["GlobalConstants"].isLoggedIn;
        this.getConnector();
    }
    showServerTabButton(tab) {
        this.showTab1 = false;
        this.showTab2 = false;
        if (tab === 'Server') {
            this.showTab1 = true;
        }
        if (tab === 'ConnectionPool') {
            this.showTab2 = true;
        }
    }
    getConnector() {
        console.log('ConnectorComponent: getConnector');
        this.restService.getConnector()
            .subscribe((data) => {
            // console.log('ConnectorComponent: getConnector', data);
            this.connector = data;
            if (this.connector.type === 'MySQL') {
                this.connectorMySql = this.connector;
            }
            else if (this.connector.type === 'SQL_Server') {
                this.connectorSqlServer = this.connector;
            }
            else {
                this.myConsole = 'Not a valid connector type';
            }
            // get old connectors so you don't have to retype everything
            if (!this.connectorMySql) {
                this.connectorSqlServer = this.getConnectorByServerType('MySQL');
            }
            if (!this.connectorSqlServer) {
                this.connectorSqlServer = this.getConnectorByServerType('SQL_Server');
            }
            this.isLoggedIn = true;
            this.isworking = false;
        });
    }
    getConnectorByServerType(serverType) {
        console.log('ConnectorComponent: getConnectorByServerType');
        this.restService.getConnectorByServerType(serverType)
            .subscribe((data) => {
            // console.log('ConnectorComponent: getConnectorByServerType', data);
            if (serverType === 'MySQL') {
                this.connectorMySql = data;
            }
            else if (serverType === 'SQL_Server') {
                this.connectorSqlServer = data;
            }
            else {
                this.myConsole = 'Not a valid connector type';
            }
        });
    }
    onOptionsSelected(event) {
        console.log('ConnectorComponent: onOptionsSelected', event.target.value);
        if (event.target.value === 'MySQL') {
            this.connector = this.connectorMySql;
        }
        else if (event.target.value === 'SQL_Server') {
            this.connector = this.connectorSqlServer;
        }
        else {
            this.myConsole = 'Not a valid connector type';
        }
    }
    saveConnection() {
        console.log('ConnectorComponent: saveConnection');
        this.isworking = true;
        this.myConsole = '';
        this.restService.postConnector(this.connector)
            .subscribe(data => {
            this.myConsole = data;
            if (this.myConsole.Exception) {
                this.myConsole = 'Exception:\n' + atob(this.myConsole.Exception);
            }
            else {
                this.myConsole = this.myConsole.response;
            }
            this.isworking = false;
        });
    }
    updateConnectionString() {
        console.log('ConnectorComponent: connectionString');
        if (this.connector.type === 'MySQL') {
            // tslint:disable-next-line: max-line-length
            this.connector.connectionString = 'jdbc:mysql://' + this.connector.hostname + ':' + this.connector.port + '/' + this.connector.database + '?serverTimezone=UTC&useLegacyDatetimeCode=false&useSSL=FALSE&user=' + this.connector.username + '&password=' + this.connector.password + '';
        }
        else if (this.connector.type === 'SQL_Server') {
            // tslint:disable-next-line: max-line-length
            this.connector.connectionString = 'jdbc:sqlserver://' + this.connector.hostname + ':' + this.connector.port + ';databaseName=' + this.connector.database + ';user=' + this.connector.username + ';password=' + this.connector.password + ';';
        }
        else {
            this.myConsole = 'Not a valid connector type';
        }
    }
};
ConnectorComponent.ctorParameters = () => [
    { type: _restservice_service__WEBPACK_IMPORTED_MODULE_2__["RestserviceService"] }
];
ConnectorComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-connector',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./connector.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/connector/connector.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./connector.component.css */ "./src/app/connector/connector.component.css")).default]
    })
], ConnectorComponent);



/***/ }),

/***/ "./src/app/endpoint/endpoint.component.css":
/*!*************************************************!*\
  !*** ./src/app/endpoint/endpoint.component.css ***!
  \*************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2VuZHBvaW50L2VuZHBvaW50LmNvbXBvbmVudC5jc3MifQ== */");

/***/ }),

/***/ "./src/app/endpoint/endpoint.component.ts":
/*!************************************************!*\
  !*** ./src/app/endpoint/endpoint.component.ts ***!
  \************************************************/
/*! exports provided: EndpointComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "EndpointComponent", function() { return EndpointComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _restservice_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../restservice.service */ "./src/app/restservice.service.ts");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");




let EndpointComponent = class EndpointComponent {
    constructor(restService) {
        this.restService = restService;
        this.isworking = true;
        this.showTab1 = true;
        this.endpoints = [];
        this.endpoint = {};
        this.basicAuth = {};
        this.myConsole = '';
        this.base64ValuePlaceholder = '';
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_3__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        console.log('EndpointComponent: ngOnInit', this.isLoggedIn);
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_3__["GlobalConstants"].isLoggedIn;
        this.myConsole = '';
        this.getBasicAuth();
        this.getEndpoints();
    }
    showServerTabButton(tab) {
        console.log('EndpointComponent: showServerTabButton');
        this.myConsole = '';
        this.showTab1 = false;
        this.showTab2 = false;
        if (tab === 'tab1') {
            this.showTab1 = true;
        }
        if (tab === 'tab2') {
            this.showTab2 = true;
        }
    }
    addEndpoint() {
        console.log('EndpointComponent: addEndpoint');
        this.myConsole = '';
        this.showServerTabButton('tab1');
        const newEndpoint = {};
        newEndpoint.name = this.uuidv4();
        newEndpoint.endpoint = '/rest/webexcc/' + newEndpoint.name;
        newEndpoint.nameValueList = [];
        this.endpoints.push(newEndpoint);
        this.endpoint = newEndpoint;
        this.endpoint.query = 'select ${ani}';
        this.addNameValuePair({ name: 'ani', value: '1234567890' });
        this.highlightEndpoint();
    }
    deleteEndpoint(index) {
        console.log('EndpointComponent: deleteEndpoint:', index);
        const answer = window.confirm('Are sure you want to delete this item ?');
        if (answer) {
            this.isworking = true;
            this.myConsole = '';
            this.restService.deleteEndpoint(this.endpoints[index])
                .subscribe(data => {
                this.basicAuth = data;
                if (this.basicAuth.Exception) {
                    this.myConsole = atob(this.myConsole.Exception);
                }
                else {
                    this.myConsole = 'Basic authentication was loaded.\n';
                }
                this.endpoints.splice(index, 1);
                this.isworking = false;
            });
        }
        else {
            console.log('EndpointComponent: deleteEndpoint: NOT');
        }
    }
    addNameValuePair(nameValue) {
        console.log('EndpointComponent: addNameValue:');
        this.myConsole = '';
        this.endpoint.nameValueList.push(nameValue);
    }
    deleteNameValuePair(index) {
        console.log('EndpointComponent: deleteNameValuePair:');
        const answer = window.confirm('Are sure you want to delete this item ?');
        if (answer) {
            this.myConsole = '';
            this.endpoint.nameValueList.splice(index, 1);
            this.myConsole = 'Name value pair deleted';
        }
        else {
            console.log('EndpointComponent: deleteNameValuePair: NOT');
        }
    }
    getBasicAuth() {
        console.log('EndpointComponent: getBasicAuth');
        this.myConsole = '';
        this.restService.getBasicAuth()
            .subscribe(data => {
            this.basicAuth = data;
            if (this.basicAuth.Exception) {
                this.myConsole = atob(this.myConsole.Exception);
            }
            else {
                this.myConsole = 'Basic authentication was loaded.\n';
            }
            this.updateBasicAuthValue();
        });
    }
    getEndpoints() {
        console.log('EndpointComponent: getEndpoints');
        this.myConsole = '';
        this.restService.getEndpoints()
            .subscribe(data => {
            this.endpoints = data;
            if (this.endpoints.Exception) {
                this.myConsole = atob(this.myConsole.Exception);
            }
            else {
                this.myConsole += 'Endpoints were loaded.\n';
            }
            if (this.endpoints[0] != null) {
                this.endpoint = this.endpoints[0];
                this.endpoint.isHighlighted = true;
            }
            this.isLoggedIn = true;
            this.isworking = false;
        });
    }
    uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            const r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    saveBasicAuthentication() {
        console.log('EndpointComponent: saveBasicAuthentication');
        this.isworking = true;
        this.myConsole = '';
        this.restService.saveBasicAuthentication(this.basicAuth)
            .subscribe(data => {
            this.myConsole = data;
            if (this.myConsole.Exception) {
                this.myConsole = 'Exception:\n' + atob(this.myConsole.Exception);
            }
            else {
                this.myConsole = this.myConsole.response;
            }
            this.isworking = false;
        });
    }
    isNameValuePairValid() {
        let check = true;
        this.endpoint.nameValueList.forEach((nameValue) => {
            if (nameValue.name == null || nameValue.name === undefined || nameValue.name.length < 1) {
                alert('Invalid name value pair:\n Name can\'t be blank.');
                check = false;
            }
            else if (!isNaN(nameValue.name)) {
                alert('Invalid name value pair:\nName can\'t be a number.');
                check = false;
            }
            else if (/^[0-9-]/.test(nameValue.name)) {
                alert('Invalid name value pair:\nName can\'t start with a number or dash');
                check = false;
            }
            else if (/[ !@#$%^&*()+\=\[\]{};':"\\|,.<>\/?]/g.test(nameValue.name)) {
                alert('Invalid name value pair:\nName can only contain the following characters be a-z A-Z 0-9.');
                check = false;
            }
        });
        return check;
    }
    saveEndpointAndTest() {
        console.log('EndpointComponent: saveEndpointAndTest');
        this.myConsole = '';
        this.endpoint.isHighlighted = undefined;
        if (!this.isNameValuePairValid()) {
            return;
        }
        this.isworking = true;
        this.restService.saveEndpointAndTest(this.endpoint)
            .subscribe(data => {
            this.myConsole = data;
            if (this.myConsole.Exception) {
                this.myConsole = 'Exception:\n' + atob(this.myConsole.Exception);
            }
            else {
                const response = this.myConsole.response;
                const params = atob(this.myConsole.httpParams);
                const sqlStatement = atob(this.myConsole.sqlStatement);
                const authentication = this.myConsole.authentication;
                const jsonResponse = atob(this.myConsole.jsonResponse);
                this.myConsole = response + '\n';
                this.myConsole += 'Endpoint authentication is active:' + authentication + '\n';
                this.myConsole += 'Request:\n';
                this.myConsole += location.origin + this.endpoint.endpoint + params + '\n';
                this.myConsole += 'Response:\n';
                this.myConsole += jsonResponse + '\n';
                this.myConsole += 'SQL:\n';
                this.myConsole += sqlStatement + '\n';
            }
            this.endpoint.isHighlighted = true;
            this.isworking = false;
        });
    }
    updateBasicAuthValue() {
        console.log('EndpointComponent: basicAuthValue');
        this.myConsole = '';
        if (this.basicAuth.username.length < 12 || this.basicAuth.password.length < 12) {
            this.base64ValuePlaceholder = 'username and password must be 12 charactors';
            this.basicAuth.value = '';
        }
        else {
            this.basicAuth.value = btoa(this.basicAuth.username + ':' + this.basicAuth.password);
        }
    }
    selectEndpoint(endpoint) {
        console.log('EndpointComponent: selectEndpoint');
        this.myConsole = '';
        this.endpoint = endpoint;
        this.highlightEndpoint();
    }
    setBasicAuthenticationRequired(event) {
        console.log('EndpointComponent: setBasicAuthenticationRequired');
        this.saveBasicAuthentication();
    }
    highlightEndpoint() {
        console.log('EndpointComponent: highlightEndpoint');
        this.endpoints.forEach((endpoint) => {
            // console.warn('endpoint:', endpoint);
            endpoint.isHighlighted = false;
        });
        this.endpoint.isHighlighted = true;
    }
};
EndpointComponent.ctorParameters = () => [
    { type: _restservice_service__WEBPACK_IMPORTED_MODULE_2__["RestserviceService"] }
];
EndpointComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-endpoint',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./endpoint.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/endpoint/endpoint.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./endpoint.component.css */ "./src/app/endpoint/endpoint.component.css")).default]
    })
], EndpointComponent);



/***/ }),

/***/ "./src/app/global-constants.ts":
/*!*************************************!*\
  !*** ./src/app/global-constants.ts ***!
  \*************************************/
/*! exports provided: GlobalConstants */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "GlobalConstants", function() { return GlobalConstants; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");

class GlobalConstants {
}
GlobalConstants.isLoggedIn = false;


/***/ }),

/***/ "./src/app/grid-view/grid-view.component.css":
/*!***************************************************!*\
  !*** ./src/app/grid-view/grid-view.component.css ***!
  \***************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2dyaWQtdmlldy9ncmlkLXZpZXcuY29tcG9uZW50LmNzcyJ9 */");

/***/ }),

/***/ "./src/app/grid-view/grid-view.component.ts":
/*!**************************************************!*\
  !*** ./src/app/grid-view/grid-view.component.ts ***!
  \**************************************************/
/*! exports provided: GridViewComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "GridViewComponent", function() { return GridViewComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _restservice_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../restservice.service */ "./src/app/restservice.service.ts");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");




let GridViewComponent = class GridViewComponent {
    constructor(restService) {
        this.restService = restService;
        this.endPoint = [];
        this.endpoints = [];
        this.endpoint = {};
        this.basicAuth = {};
        this.selectedEndPoint = '';
        this.records = [{}];
        this.headers = [];
        this.isworking = true;
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_3__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        this.getBasicAuth();
        this.getEndpoints();
    }
    getBasicAuth() {
        console.log('GridViewComponent: getBasicAuth');
        this.restService.getBasicAuth()
            .subscribe(data => {
            this.basicAuth = data;
            // if (this.basicAuth.Exception) {
            //   this.myConsole = atob(this.myConsole.Exception);
            // } else {
            //   this.myConsole = 'Basic authentication was loaded.\n';
            // }
            // this.updateBasicAuthValue();
        });
    }
    getEndpoints() {
        console.log('GridViewComponent: getEndpoints');
        this.restService.getEndpoints()
            .subscribe(data => {
            this.endpoints = data;
            console.log('GridViewComponent: getEndpoints', this.endpoints);
            // if (this.endpoints.Exception) {
            //   this.myConsole = atob(this.myConsole.Exception);
            // } else {
            //   this.myConsole += 'Endpoints were loaded.\n';
            // }
            // if (this.endpoints[0] != null) {
            //   this.endpoint = this.endpoints[0];
            //   this.endpoint.isHighlighted = true;
            // }
            this.isLoggedIn = true;
            this.isworking = false;
        });
    }
    executeEndPoint(event) {
        console.log('GridViewComponent: executeEndPoint', event.target.value);
        // console.log('GridViewComponent: executeEndPoint', this.selectedEndPoint);
        let tmp = [];
        this.records = [];
        this.restService.executeEndPoint(event.target.value)
            .subscribe(data => {
            // console.log('GridViewComponent: executeEndPoint', data);
            const check = data;
            // try {
            if (check.length > 0) {
                tmp = data;
            }
            else {
                tmp.push(data);
            }
            // } catch (error) {
            //   this.records.push(data);
            // }
            // console.log('GridViewComponent: executeEndPoint: length:', this.records.length);
            let count = 0;
            tmp.forEach((element) => {
                if (count++ === 0) {
                    this.headers = Object.keys(element);
                }
                this.records.push(Object.values(element));
                console.log('GridViewComponent: executeEndPoint: this.records4: ', Object.values(element));
            });
        });
    }
};
GridViewComponent.ctorParameters = () => [
    { type: _restservice_service__WEBPACK_IMPORTED_MODULE_2__["RestserviceService"] }
];
GridViewComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-grid-view',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./grid-view.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/grid-view/grid-view.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./grid-view.component.css */ "./src/app/grid-view/grid-view.component.css")).default]
    })
], GridViewComponent);



/***/ }),

/***/ "./src/app/help/help.component.css":
/*!*****************************************!*\
  !*** ./src/app/help/help.component.css ***!
  \*****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2hlbHAvaGVscC5jb21wb25lbnQuY3NzIn0= */");

/***/ }),

/***/ "./src/app/help/help.component.ts":
/*!****************************************!*\
  !*** ./src/app/help/help.component.ts ***!
  \****************************************/
/*! exports provided: HelpComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "HelpComponent", function() { return HelpComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");



let HelpComponent = class HelpComponent {
    constructor() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        console.log('HelpComponent: ngOnInit', this.isLoggedIn);
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    clickMe() {
        // const answer = window.confirm('Are sure you want to delete this item ?');
        // console.log('clickMe', answer);
    }
};
HelpComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-help',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./help.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/help/help.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./help.component.css */ "./src/app/help/help.component.css")).default]
    })
], HelpComponent);



/***/ }),

/***/ "./src/app/loggedin/loggedin.component.css":
/*!*************************************************!*\
  !*** ./src/app/loggedin/loggedin.component.css ***!
  \*************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2xvZ2dlZGluL2xvZ2dlZGluLmNvbXBvbmVudC5jc3MifQ== */");

/***/ }),

/***/ "./src/app/loggedin/loggedin.component.ts":
/*!************************************************!*\
  !*** ./src/app/loggedin/loggedin.component.ts ***!
  \************************************************/
/*! exports provided: LoggedinComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoggedinComponent", function() { return LoggedinComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");



let LoggedinComponent = class LoggedinComponent {
    constructor() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
};
LoggedinComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-loggedin',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./loggedin.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/loggedin/loggedin.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./loggedin.component.css */ "./src/app/loggedin/loggedin.component.css")).default]
    })
], LoggedinComponent);



/***/ }),

/***/ "./src/app/login/login.component.css":
/*!*******************************************!*\
  !*** ./src/app/login/login.component.css ***!
  \*******************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2xvZ2luL2xvZ2luLmNvbXBvbmVudC5jc3MifQ== */");

/***/ }),

/***/ "./src/app/login/login.component.ts":
/*!******************************************!*\
  !*** ./src/app/login/login.component.ts ***!
  \******************************************/
/*! exports provided: LoginComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginComponent", function() { return LoginComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm2015/router.js");




let LoginComponent = class LoginComponent {
    constructor(router) {
        this.router = router;
    }
    ngOnInit() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    clickMe(event) {
        console.log('clickMe', event);
    }
};
LoginComponent.ctorParameters = () => [
    { type: _angular_router__WEBPACK_IMPORTED_MODULE_3__["Router"] }
];
LoginComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-login',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./login.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/login/login.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./login.component.css */ "./src/app/login/login.component.css")).default]
    })
], LoginComponent);



/***/ }),

/***/ "./src/app/logout/logout.component.css":
/*!*********************************************!*\
  !*** ./src/app/logout/logout.component.css ***!
  \*********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL2xvZ291dC9sb2dvdXQuY29tcG9uZW50LmNzcyJ9 */");

/***/ }),

/***/ "./src/app/logout/logout.component.ts":
/*!********************************************!*\
  !*** ./src/app/logout/logout.component.ts ***!
  \********************************************/
/*! exports provided: LogoutComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LogoutComponent", function() { return LogoutComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");



let LogoutComponent = class LogoutComponent {
    constructor() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        console.log('LogoutComponent: ngOnInit', this.isLoggedIn);
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
        this.message = '';
    }
    myLogout() {
        console.log('LogoutComponent: myLogout');
        window.location.href = '/oauthLogout';
    }
};
LogoutComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-logout',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./logout.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/logout/logout.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./logout.component.css */ "./src/app/logout/logout.component.css")).default]
    })
], LogoutComponent);



/***/ }),

/***/ "./src/app/restservice.service.ts":
/*!****************************************!*\
  !*** ./src/app/restservice.service.ts ***!
  \****************************************/
/*! exports provided: RestserviceService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "RestserviceService", function() { return RestserviceService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm2015/http.js");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! rxjs */ "./node_modules/rxjs/_esm2015/index.js");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! rxjs/operators */ "./node_modules/rxjs/_esm2015/operators/index.js");
var RestserviceService_1;



// import { HttpClient, , HttpErrorResponse } from '@angular/common/http';


// import { Location } from '@angular/common';
// import { Router } from '@angular/router';
// const httpOptions = {
//   headers: new HttpHeaders({
//     'Set-Cookie': 'SameSite=Strict; Secure',
//   })
// };
let RestserviceService = RestserviceService_1 = class RestserviceService {
    constructor(http) {
        this.http = http;
        this.onLoginChange = new _angular_core__WEBPACK_IMPORTED_MODULE_1__["EventEmitter"]();
    }
    getUser() {
        console.log('RestserviceService: getUser');
        return this.http.get('/user/user').pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    isAuthenticated() {
        console.log('RestserviceService: isAuthenticated');
        this.getUser().subscribe(data => {
            // console.warn('restService.getUser:1', data);
            if (data == null) {
                RestserviceService_1.isLoggedIn = false;
            }
            else {
                const response = data;
                console.warn('restService.getUser: data', data);
                if (response.response === 'true') {
                    RestserviceService_1.isLoggedIn = true;
                }
                else {
                    RestserviceService_1.isLoggedIn = false;
                }
            }
            this.onLoginChange.emit(RestserviceService_1.isLoggedIn);
        });
    }
    getIsLoggedIn() {
        console.log('RestserviceService: getIsLoggedIn');
        return RestserviceService_1.isLoggedIn;
    }
    getConnector() {
        return this.http.get('/rest/connector').pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    getConnectorByServerType(serverType) {
        return this.http.get('/rest/connector/' + serverType).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    postConnector(connector) {
        return this.http.post('/rest/connector', connector).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    getBasicAuth() {
        return this.http.get('/rest/basicauth').pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    getEndpoints() {
        return this.http.get('/rest/endpoints').pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    executeEndPoint(endpointName) {
        return this.http.get('/rest/webexcc/' + endpointName).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    saveBasicAuthentication(basicAuth) {
        return this.http.post('/rest/basicauth', basicAuth).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    saveEndpointAndTest(endpoint) {
        return this.http.post('/rest/endpoint', endpoint).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    deleteEndpoint(endpoint) {
        return this.http.delete('/rest/endpoint/' + endpoint.name).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_4__["catchError"])(this.handleError));
    }
    handleError(error) {
        console.warn('RestserviceService: private handleError: 1 :', error);
        if (error.error instanceof ErrorEvent) {
            // A client-side or network error occurred. Handle it accordingly.
            console.warn('RestserviceService: private handleError: 2:', error);
        }
        else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong.
            console.warn('RestserviceService: private handleError: 3 :Backend returned code ' + error.status + ' ' + 'body was: ' + error.error);
            if (error.status === 0) {
                console.warn('RestserviceService: private handleError: 3b redirect to /login');
                location.assign('/oauth2/authorization/dbconnector');
            }
        }
        // Return an observable with a user-facing error message.
        return Object(rxjs__WEBPACK_IMPORTED_MODULE_3__["throwError"])('{"Exception":" ' + window.btoa('Something bad happened; please try again later.') + '"}');
    }
};
RestserviceService.ctorParameters = () => [
    { type: _angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpClient"] }
];
RestserviceService.isLoggedIn = false;
RestserviceService = RestserviceService_1 = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
        providedIn: 'root'
    })
], RestserviceService);



/***/ }),

/***/ "./src/app/support/support.component.css":
/*!***********************************************!*\
  !*** ./src/app/support/support.component.css ***!
  \***********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony default export */ __webpack_exports__["default"] = ("\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJzcmMvYXBwL3N1cHBvcnQvc3VwcG9ydC5jb21wb25lbnQuY3NzIn0= */");

/***/ }),

/***/ "./src/app/support/support.component.ts":
/*!**********************************************!*\
  !*** ./src/app/support/support.component.ts ***!
  \**********************************************/
/*! exports provided: SupportComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SupportComponent", function() { return SupportComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _global_constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../global-constants */ "./src/app/global-constants.ts");



let SupportComponent = class SupportComponent {
    constructor() {
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
    ngOnInit() {
        console.log('SupportComponent: ngOnInit', this.isLoggedIn);
        this.isLoggedIn = _global_constants__WEBPACK_IMPORTED_MODULE_2__["GlobalConstants"].isLoggedIn;
    }
};
SupportComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
        selector: 'app-support',
        template: tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! raw-loader!./support.component.html */ "./node_modules/raw-loader/dist/cjs.js!./src/app/support/support.component.html")).default,
        styles: [tslib__WEBPACK_IMPORTED_MODULE_0__["__importDefault"](__webpack_require__(/*! ./support.component.css */ "./src/app/support/support.component.css")).default]
    })
], SupportComponent);



/***/ }),

/***/ "./src/environments/environment.ts":
/*!*****************************************!*\
  !*** ./src/environments/environment.ts ***!
  \*****************************************/
/*! exports provided: environment */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "environment", function() { return environment; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

const environment = {
    production: false
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.


/***/ }),

/***/ "./src/main.ts":
/*!*********************!*\
  !*** ./src/main.ts ***!
  \*********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/platform-browser-dynamic */ "./node_modules/@angular/platform-browser-dynamic/fesm2015/platform-browser-dynamic.js");
/* harmony import */ var _app_app_module__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./app/app.module */ "./src/app/app.module.ts");
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./environments/environment */ "./src/environments/environment.ts");





if (_environments_environment__WEBPACK_IMPORTED_MODULE_4__["environment"].production) {
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["enableProdMode"])();
}
Object(_angular_platform_browser_dynamic__WEBPACK_IMPORTED_MODULE_2__["platformBrowserDynamic"])().bootstrapModule(_app_app_module__WEBPACK_IMPORTED_MODULE_3__["AppModule"])
    .catch(err => console.error(err));


/***/ }),

/***/ 0:
/*!***************************!*\
  !*** multi ./src/main.ts ***!
  \***************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(/*! /Users/jiwyatt/git/ps/dbconnector/src/main.ts */"./src/main.ts");


/***/ })

},[[0,"runtime","vendor"]]]);
//# sourceMappingURL=main-es2015.js.map