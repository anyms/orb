# Orb - Browser for Android

Easily embedable android browser based on `adblockplus` webview. Find out more about android `adblockplus` at this link:

[https://github.com/adblockplus/libadblockplus-android](https://github.com/adblockplus/libadblockplus-android)


### Getting Started

Add this in your **project level** `build.gradle`.

```css
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your **app level** `build.gradle`

```css
implementation 'com.github.defapps:orb:1.0.1'
```

### TODO

- [ ] Control webview from browser
- [ ] Add toolbar with all widgets (Search Bar, Tabs View, Menu)
- [ ] Clear all tabs feature
- [ ] Add tab system (New Tab, Switch Tab, Restore Tab)
- [ ] Create default page for blank tab
- [ ] explain the usage
