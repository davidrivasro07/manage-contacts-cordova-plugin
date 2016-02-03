/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

#import "CDVManageContacts.h"
#import <UIKit/UIKit.h>
#import <Cordova/NSArray+Comparisons.h>
#import <Cordova/NSDictionary+Extensions.h>
#import <ContactsUI/ContactsUI.h>

@implementation CDVManageContacts

- (void)add:(CDVInvokedUrlCommand*)command
{

  //NSArray <CNLabeledValue<CNPhoneNumber *> *> *phoneNumbers = @[phoneNumber, phoneNumber2];

  CNMutableContact * contact = [[CNMutableContact alloc] init];
  NSMutableArray <CNLabeledValue<CNPhoneNumber *> *> *phoneNumbers = [[NSMutableArray alloc] init];

  NSArray *separatedName = [[command argumentAtIndex:0][@"name"] componentsSeparatedByCharactersInSet:
                      [NSCharacterSet characterSetWithCharactersInString:@" "]
                    ];

  if([separatedName count] == 1)
  {
    contact.givenName = [separatedName objectAtIndex:0];
  }
  else if([separatedName count] == 2)
  {
    contact.givenName = [separatedName objectAtIndex:0];
    contact.familyName = [separatedName objectAtIndex:1];
  }
  else if([separatedName count] == 3)
  {
    contact.givenName = [separatedName objectAtIndex:0];
    contact.familyName = [NSString stringWithFormat:@"%@ %@", [separatedName objectAtIndex:1], [separatedName objectAtIndex:2]];
  }

  for (id phone in [command argumentAtIndex:0][@"phones"]) {
      //NSArray *userApollo = user;

      CNPhoneNumber *number = [[CNPhoneNumber alloc] initWithStringValue:phone[@"number"]];
      NSString *label = phone[@"label"];
      CNLabeledValue *phoneNumber = [[CNLabeledValue alloc] initWithLabel:label value:number];

      [phoneNumbers addObject:phoneNumber];
  }


  contact.phoneNumbers = phoneNumbers;

  CNContactViewController *addContactVC = [CNContactViewController viewControllerForNewContact:contact];
  addContactVC.delegate                 = self;
  UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:addContactVC];
  [self.viewController presentViewController:navController animated:YES completion:nil];



  return;
}

- (void)microphone:(CDVInvokedUrlCommand*)command
{
  [[AVAudioSession sharedInstance] requestRecordPermission:^(BOOL granted) {
        if (granted) {
            NSLog(@"Permission granted");
        }
        else {
            NSLog(@"Permission denied");
        }
    }];
  return;
}

- (void)contactViewController:(CNContactViewController *)viewController
	   didCompleteWithContact:(CNContact *)contact{

	[self.viewController dismissModalViewControllerAnimated:YES];

}
@end
