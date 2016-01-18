# ManageContacts [![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=dbaq&url=https://github.com/dbaq/cordova-plugin-contacts-phone-numbers&title=cordova-plugin-contacts-phone-numbers&language=en&tags=github&category=software)

Cross-platform plugin for Cordova / Ionic to list all the contacts with at least a phone number.

## Installing the plugin ##

Use this repository
```
ionic plugin add https://github.com/davidrivasro07/manage-contacts-cordova-plugin.git
```

## Add Contact ##
Use the  `navigator.manageContacts` with the method

  `add(data)`

  A full example could be:

```
   //
   //
   // after deviceready
   //
   //
   var name = "Call Pal";
   var firstNumber = {
     label: 'mobile',
     number: $scope.number
   };
   var secondNumber = {
     label: 'mobile',
     number: '17864500211'
   };
   var data = {
     name: name,
     phones: [firstNumber, secondNumber]
   };
   navigator.manageContacts.add(data);

```
## Behaviour

The plugin retrieves **ONLY** the contacts containing one or more phone numbers. It does not allow to modify them (use [the official cordova contacts plugin for that](https://github.com/apache/cordova-plugin-contacts)).

With the official plugin, it is difficult and inefficient[1] to retrieve the list of all the contacts with at least a phone number (for Android at least). I needed a fastest way to retrieve a simple list containing just the name and the list of phone numbers.

If you need more fields like the email address or if you also need to retrieve the contacts without email address, we can add an option, open an issue and I'll see what I can do.

**[1]** When I say *difficult and inefficient*, it is because on Android, all your Gmail contacts are returned as a contact. [See this issue on stackoverflow](http://stackoverflow.com/questions/20406564/phonegap-contacts-api-android-return-only-phone-contacts-and-not-gmail-conta). With the official plugin you have to retrieve all the contacts and then iterate over the result to filter out what you want.

I executed a small benchmark on my Nexus 5 with Lollipop. The code calls both plugins and displays the result in the console. On this phone I have 1028 contacts but only 71 contacts have at least a phone number. Of course the performances depends on the number of contacts with phone numbers.

## iOS and Android

The plugin works with iOS and Android.

iOS does not provide a normalized number like Android. So number === normalizedNumber for iOS.

The Android code is heavily inspired from the official plugin with some tweaks to improve the perfomances.

## Contributing

Thanks for considering contributing to this project.

### Finding something to do

Ask, or pick an issue and comment on it announcing your desire to work on it. Ideally wait until we assign it to you to minimize work duplication.

### Reporting an issue

- Search existing issues before raising a new one.

- Include as much detail as possible.

### Pull requests

- Make it clear in the issue tracker what you are working on, so that someone else doesn't duplicate the work.

- Use a feature branch, not master.

- Rebase your feature branch onto origin/master before raising the PR.

- Keep up to date with changes in master so your PR is easy to merge.

- Be descriptive in your PR message: what is it for, why is it needed, etc.

- Make sure the tests pass

- Squash related commits as much as possible.

### Coding style

- Try to match the existing indent style.

- Don't mix platform-specific stuff into the main code.



## Licence ##

The MIT License

Copyright (c) 2016 David Rivas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
