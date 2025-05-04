import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialog, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle } from '@angular/material/dialog';

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrl: './alert.component.css',
    imports: [MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose, MatButtonModule],
})
export class AlertComponent {
    readonly data = inject<{ title: string, message: string[] }>(MAT_DIALOG_DATA);
}

export const alertBuilder = (dialogRef: MatDialog) => (title: string, ...message: string[]) => {
    return dialogRef.open(AlertComponent, { data: { title, message, } });
};

export const commonHttpErrorHanlder = (dialogRef: MatDialog) => (hhtpResp: HttpErrorResponse) => {
    if (hhtpResp.error.errorDetail) {
        return dialogRef.open(AlertComponent, {
            data: {
                title: "Error",
                message: hhtpResp.error.errorDetail,
            },
            autoFocus: false,
        });
    }
    if (hhtpResp.error.errorMessage) {
        return dialogRef.open(AlertComponent, {
            data: {
                title: "Error",
                message: [hhtpResp.error.errorMessage],
            }
        });
    }
    return dialogRef.open(AlertComponent, {
        data: {
            title: "Error " + hhtpResp.status,
            message: [hhtpResp.message],
        }
    });
};
