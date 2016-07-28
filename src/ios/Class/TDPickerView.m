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
    NSArray *_dataArray;
}

@end

@implementation TDPickerView

- (void)tdShowPickerView:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    
    NSArray *paramArr = command.arguments;
    NSString *jsonStr = [paramArr objectAtIndex:0];
    _dataArray = [self arrayWithJsonString:jsonStr];
    
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
    return _dataArray.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    NSDictionary *dic = [_dataArray objectAtIndex:row];
    return dic[@"key"];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    NSDictionary *dic = [_dataArray objectAtIndex:row];
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:dic[@"key"]];
    [self.commandDelegate sendPluginResult:result callbackId:_callbackId];
}

/*!
 * @brief 把格式化的JSON格式的字符串转换成数组
 * @param jsonString JSON格式的字符串
 * @return 返回数组
 */
- (NSArray *)arrayWithJsonString:(NSString *)jsonString {
    if (jsonString == nil) {
        return nil;
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSArray *arr = [NSJSONSerialization JSONObjectWithData:jsonData
                                                   options:NSJSONReadingMutableContainers
                                                     error:&err];
    if(err) {
        NSLog(@"json解析失败：%@",err);
        return [NSArray array];
    }
    return arr;
}

@end
