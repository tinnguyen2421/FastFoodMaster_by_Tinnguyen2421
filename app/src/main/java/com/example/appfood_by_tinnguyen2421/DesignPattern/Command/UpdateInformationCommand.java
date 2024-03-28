package com.example.appfood_by_tinnguyen2421.DesignPattern.Command;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerEditProfile;

public class UpdateInformationCommand implements Command {
    private CustomerEditProfile customerEditProfile;

    public UpdateInformationCommand(CustomerEditProfile customerEditProfile) {
        this.customerEditProfile = customerEditProfile;
    }
    @Override
    public void execute() {
        customerEditProfile.updateInformation();
    }
}
