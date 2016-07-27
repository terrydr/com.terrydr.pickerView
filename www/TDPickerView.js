var tdPickerView= {
tdShowPIckerView: function(successCallback, errorCallback) {
    console.log("tdShowPIckerView");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TDPickerView",
                 "tdShowPickerView",
                 []
                 );
    
}
    
}

module.exports = tdPickerView;
