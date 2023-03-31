package hardcoder.dev.androidApp.ui.features.pedometer

import hardcoder.dev.healther.R
import hardcoder.dev.presentation.features.pedometer.RejectReason
import hardcoder.dev.uikit.sections.Rejected

class PedometerRejectedMapper {

    private object PedometerDefaults {
        val BatteryRejected = Rejected(
            animationResId = R.raw.permissions_not_granted,
            titleResId = R.string.pedometer_reject_battery_title,
            descriptionResId = R.string.pedometer_reject_battery_description
        )
        val PermissionsRejected = Rejected(
            animationResId = R.raw.permissions_not_granted,
            titleResId = R.string.pedometer_reject_permissions_title,
            descriptionResId = R.string.pedometer_reject_permissions_description
        )
        val ServiceNotAvailableRejected = Rejected(
            animationResId = R.raw.permissions_not_granted,
            titleResId = R.string.pedometer_reject_not_available_title,
            descriptionResId = R.string.pedometer_reject_not_available_description
        )
    }

    fun mapReasonToRejected(rejectReason: RejectReason, isInitialScreenShowed: Boolean): Rejected? {
        return if (isInitialScreenShowed.not()) {
            when (rejectReason) {
                RejectReason.BatteryNotIgnoreOptimizations -> PedometerDefaults.BatteryRejected
                RejectReason.PermissionsNotGranted -> PedometerDefaults.PermissionsRejected
                RejectReason.ServiceNotAvailable -> PedometerDefaults.ServiceNotAvailableRejected
            }
        } else {
            null
        }
    }
}