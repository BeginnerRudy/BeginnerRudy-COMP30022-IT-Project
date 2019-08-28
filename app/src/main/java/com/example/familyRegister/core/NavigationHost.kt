package com.example.familyRegister.core

import androidx.fragment.app.Fragment


/**
 * A host (typically an `Activity`} that can display fragments and knows how to respond to
 * navigation events.
 *
 * Used for navigation from register fragment to category fragment
 */

interface NavigationHost {
    /**
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible.
     */
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}
