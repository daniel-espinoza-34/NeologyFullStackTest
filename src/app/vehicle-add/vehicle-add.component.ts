import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
    selector: 'app-vehicle-add',
    imports: [MatFormFieldModule, MatInputModule, FormsModule, ReactiveFormsModule, MatButtonModule, MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose],
    templateUrl: './vehicle-add.component.html',
    styleUrl: './vehicle-add.component.css'
})
export class VehicleAddComponent {
    httpClient = inject(HttpClient);
    data = inject<{ vehicleType: string }>(MAT_DIALOG_DATA);
    licensePlateFormControl = new FormControl('', [
        Validators.required,
        Validators.minLength(6),
        Validators.pattern(/^[A-z0-9][A-z0-9\-]*\-[A-z0-9\-]*[A-z0-9]+$/)
    ]);

    constructor(private dialogRef: MatDialogRef<VehicleAddComponent>) { }

    onSubmit() {
        if (this.licensePlateFormControl.valid) {
            let params = JSON.stringify({ licensePlate: this.licensePlateFormControl.value!.toUpperCase() });
            this.httpClient.post(
                `http://localhost:8080/neo/vehiculos/${this.data.vehicleType}${this.data.vehicleType.endsWith('e') ? "s" : "es"}`,
                params,
                { headers: { "Content-Type": "application/json" } })
                .subscribe(result => this.dialogRef.close(result));
        }
    }
}
