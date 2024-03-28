package com.example.appfood_by_tinnguyen2421.DesignPattern.Command;

import com.example.appfood_by_tinnguyen2421.Customerr.CustomerActivity.CustomerEditProfile;

public class ChangePasswordCommand implements Command {
    private CustomerEditProfile customerEditProfile;

    public ChangePasswordCommand(CustomerEditProfile customerEditProfile1) {
        this.customerEditProfile = customerEditProfile1;
    }
    @Override
    public void execute() {
        customerEditProfile.changePassword();
    }
}
