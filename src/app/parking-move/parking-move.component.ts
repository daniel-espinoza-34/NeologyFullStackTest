import { HttpClient } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef, } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTimepickerModule } from '@angular/material/timepicker';
import { commonHttpErrorHanlder } from '../common/alert/alert.component';

@Component({
    selector: 'app-parking-move',
    templateUrl: './parking-move.component.html',
    styleUrl: './parking-move.component.css',
    providers: [provideNativeDateAdapter()],
    imports: [
        FormsModule, ReactiveFormsModule,
        MatFormFieldModule, MatInputModule, MatButtonModule,
        MatDialogModule, MatDatepickerModule, MatTimepickerModule,
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ParkingMoveComponent {
    readonly httpClient = inject(HttpClient);
    readonly dialog = inject(MatDialog);
    readonly data = inject<{ licensePlate: string, type: "entrada" | "salida" }>(MAT_DIALOG_DATA);
    readonly dateFormControl = new FormControl(new Date());

    constructor(readonly dialogRef: MatDialogRef<ParkingMoveComponent>) { }

    onSubmit() {
        if (this.dateFormControl.valid) {
            this.httpClient.post(
                `http://localhost:8080/neo/estancias/${this.data.type}`,
                JSON.stringify({
                    licensePlate: this.data.licensePlate,
                    dateTime: this.dateFormControl.value!.toISOString()
                }),
                { headers: { "Content-Type": "application/json" } })
                .subscribe({
                    next: result => this.dialogRef.close(result),
                    error: commonHttpErrorHanlder(this.dialog),
                });
        }
    }
}
