package com.minhhung.life_app.models

import com.google.gson.annotations.SerializedName

data class UpdateBudgetParams(
    @SerializedName("has_limited_outcome_per_week")
    val hasLimitedOutcomePerWeek: Boolean,
    @SerializedName("limited_outcome_per_week")
    val limitedOutcomePerWeek: Float,
    @SerializedName("has_limited_outcome_per_month")
    val hasLimitedOutcomePerMonth: Boolean,
    @SerializedName("limited_outcome_per_month")
    val limitedOutcomePerMonth: Float
)