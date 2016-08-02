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
    NSString *_callbackValue;
    NSArray *_dataArray;
}

@end

@implementation TDPickerView

- (void)tdShowPickerView:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    _callbackValue = @"";
    
    NSArray *paramArr = command.arguments;
    _dataArray = [paramArr objectAtIndex:0];
    
    UIControl *control = [self popView];
    [control addSubview:[self tdPickerContentView]];
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

- (UIView *)tdPickerContentView{
    CGRect bounds = self.viewController.view.bounds;
    UIView *contentView = [[UIView alloc] initWithFrame:CGRectMake(0, bounds.size.height - 160, bounds.size.width, 160)];
    contentView.backgroundColor = [UIColor whiteColor];
    
    UIButton *cancelBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    cancelBtn.frame = CGRectMake(10, 10, 40, 30);
    [cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
    [cancelBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [cancelBtn addTarget:self action:@selector(cancelPickerClick:) forControlEvents:UIControlEventTouchUpInside];
    [contentView addSubview:cancelBtn];
    
    UIButton *completeBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    completeBtn.frame = CGRectMake(CGRectGetWidth(bounds)-40-10, 10, 40, 30);
    [completeBtn setTitle:@"确认" forState:UIControlStateNormal];
    [completeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [completeBtn addTarget:self action:@selector(completePickerClick:) forControlEvents:UIControlEventTouchUpInside];
    [contentView addSubview:completeBtn];
    
    UIPickerView *pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 40, bounds.size.width, 120)];
    pickerView.userInteractionEnabled = YES;
    pickerView.backgroundColor = [UIColor whiteColor];
    pickerView.delegate = self;
    [contentView addSubview:pickerView];
    
    return contentView;
}

- (void)cancelPickerClick:(id)sender{
    [self mjPopupViewDismissNotify];
}

- (void)completePickerClick:(id)sender{
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:_callbackValue];
    [self.commandDelegate sendPluginResult:result callbackId:_callbackId];
    [self mjPopupViewDismissNotify];
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
    if (_dataArray.count>row) {
        NSDictionary *dic = [_dataArray objectAtIndex:row];
        _callbackValue = dic[@"key"];
    }
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
