package com.calculator.vault.gallery.locker.hide.data.ads.appLovinSupport;

/**
 * Represents callbacks for a view pager.
 * This is not part of the support library's stock pager (SdkViewPager),
 * You should set an OnPageChangeListener on the view pager and invoke these callbacks yourself.
 */
public interface InlineCarouselCardCallbacks
{
    /**
     * Called when a page becomes the foreground view.
     */
    public void onCardActivated();

    /**
     * Called when a page loses foreground focus.
     */
    public void onCardDeactivated();
}
