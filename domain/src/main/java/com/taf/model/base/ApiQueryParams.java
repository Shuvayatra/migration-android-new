package com.taf.model.base;

import com.taf.interactor.UseCaseData;

/**
 *
 */
public class ApiQueryParams {

    public String countryId;
    public String gender;

    public ApiQueryParams(UseCaseData useCaseData) {
        countryId = useCaseData.getString(UseCaseData.COUNTRY_ID, null);
        gender = useCaseData.getString(UseCaseData.USER_GENDER, null);
    }
}
