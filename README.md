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
implementation 'com.github.okbash:orb:1.0.1'
```

### Configure

- Make your theme to NoActionBar, because the browser has it's own toolbar
- For the toolbar icons color, add `<item name="colorControlNormal">#ffffff</item>` to dark theme and `<item name="colorControlNormal">#000000</item>` to light theme.

### TODO

- [ ] Control webview from browser
- [ ] Add toolbar with all widgets (Search Bar, Tabs View, Menu)
- [ ] Clear all tabs feature
- [ ] Add tab system (New Tab, Switch Tab, Restore Tab)
- [ ] Create default page for blank tab
- [ ] explain the usage
