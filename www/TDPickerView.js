var tdPickerView= {
tdShowPIckerView: function(paramData,successCallback, errorCallback) {
    console.log("tdShowPIckerView");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TDPickerView",
                 "tdShowPickerView",
                 [paramData]
                 );
    
}
    
}

module.exports = tdPickerView;
