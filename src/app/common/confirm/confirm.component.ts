import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialog, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle } from '@angular/material/dialog';
import { ConfirmProperties } from './confirm.model';

@Component({
    selector: 'app-confirm',
    templateUrl: './confirm.component.html',
    styleUrl: './confirm.component.css',
    imports: [MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose, MatButtonModule],
})
export class ConfirmComponent {
    readonly data = inject<ConfirmProperties>(MAT_DIALOG_DATA);
}

export const confirmBuilder = (dialogRef: MatDialog) => (data: ConfirmProperties) => {
    return dialogRef.open(ConfirmComponent, { data, autoFocus: false, disableClose: true });
}