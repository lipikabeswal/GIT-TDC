//
//  FirstResponder.m
//  LockDownBrowser
//
//  Created by mcgrawhill ctb on 9/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "FirstResponder.h"


@implementation FirstResponder

- (id)initWithFrame:(NSRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code here.
    }
    return self;
}

- (void)drawRect:(NSRect)rect {
    // Drawing code here.
}

- (BOOL) acceptsFirstResponder {

NSLog(@"acceptsFirstResponder......");
	return YES;
}

- (BOOL) makeFirstResponder {

NSLog(@"acceptsFirstResponder......");
	return YES;
}

@end
