//
//  UIViewController+MJPopupViewController.h
//  MJModalViewController
//
//  Created by Martin Juhasz on 11.05.12.
//  Copyright (c) 2012 martinjuhasz.de. All rights reserved.
//

#import <UIKit/UIKit.h>

#define MJPopupViewDismissNotify    @"MJPopupViewDismissNotify"

typedef enum {
    MJPopupViewAnimationSlideBottomTop = 1,
    MJPopupViewAnimationSlideRightLeft,
    MJPopupViewAnimationSlideLeftLeft,
    MJPopupViewAnimationSlideBottomBottom,
    MJPopupViewAnimationFade,
    MJPopupViewAnimationSlideTopTop
} MJPopupViewAnimation;

@interface UIViewController (MJPopupViewController)

- (void)presentPopupViewController:(UIView*)popUpView animationType:(MJPopupViewAnimation)animationType;
- (void)dismissPopupViewControllerWithanimationType:(MJPopupViewAnimation)animationType;

@end