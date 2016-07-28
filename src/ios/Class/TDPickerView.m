//
//  TDPickerView.m
//  泰瑞眼科
//
//  Created by 路亮亮 on 16/7/22.
//
//

#import "TDPickerView.h"
#import "UIViewController+MJPopupViewController.h"

@interface TDPickerView ()<UIPickerViewDataSource,UIPickerViewDelegate>{
    NSString *_callbackId;
}

@end

@implementation TDPickerView

- (void)tdShowPickerView:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    UIControl *control = [self popView];
    [control addSubview:[self tdPickerView]];
    [self.viewController presentPopupViewController:control animationType:MJPopupViewAnimationSlideBottomBottom];
}

- (UIControl *)popView {
    CGRect bounds = self.viewController.view.bounds;
    UIControl *popView = [[UIControl alloc] initWithFrame:bounds];
    [popView addTarget:self action:@selector(mjPopupViewDismissNotify) forControlEvents:UIControlEventTouchDown];
    return popView;
}

- (void)mjPopupViewDismissNotify{
    [self.viewController dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

- (UIPickerView *)tdPickerView{
    CGRect bounds = self.viewController.view.bounds;
    UIPickerView *pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, bounds.size.height - 100, bounds.size.width, 100)];
    pickerView.backgroundColor = [UIColor whiteColor];
    pickerView.delegate = self;
    return pickerView;
}

// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return 2;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return @"男";
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"男"];
    [self.commandDelegate sendPluginResult:result callbackId:_callbackId];
}

@end
