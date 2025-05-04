import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, ChangeDetectionStrategy, Component, inject, input, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { GetResidentLogResponse, ResidentInfo, ResidentPayment } from './resident-report.model';
import { timeMinutesToText } from '../../utils/utils';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTimepickerModule } from '@angular/material/timepicker';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideNativeDateAdapter } from '@angular/material/core';
import { commonHttpErrorHanlder } from '../common/alert/alert.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';

const EMPTY_RESIDENT = {
    licencePlate: '',
    accumulatedTime: 'N/A',
    accumulatedRate: 0.0,
    coveredAmount: 0.0,
    amountLeft: 0.0,
    isPending: true,
};

@Component({
    selector: 'app-resident-report',
    templateUrl: './resident-report.component.html',
    styleUrl: './resident-report.component.css',
    providers: [provideNativeDateAdapter()],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [
        RouterLink,
        FormsModule, ReactiveFormsModule, MatProgressSpinner,
        MatFormFieldModule, MatInputModule, MatButtonModule,
        MatTableModule, MatDatepickerModule, MatTimepickerModule,
    ],
})
export class ResidentReportComponent implements AfterViewInit {
    private httpClient = inject(HttpClient);
    readonly isLoading = signal(true);
    private router = inject(Router);
    readonly licensePlate = input.required<string>();
    readonly dialog = inject(MatDialog);
    readonly displayedColumns = ['id', 'paymentDate', 'paymentAmount'];
    //readonly data = inject<{ licensePlate: string }>(MAT_DIALOG_DATA);
    readonly residentInfo = signal<ResidentInfo>(EMPTY_RESIDENT);
    readonly payments = signal<ResidentPayment[]>([]);
    readonly dateFormControl = new FormControl(new Date());
    readonly amountFormControl = new FormControl(0.0);

    ngAfterViewInit(): void {
        this.loadPayments();
    }

    onSubmit() {
        if (this.dateFormControl.valid && this.amountFormControl.valid) {
            this.httpClient.post("http://localhost:8080/neo/residentes/pagos",
                JSON.stringify({
                    licensePlate: this.licensePlate(),
                    paymentDate: this.dateFormControl.value!.toISOString(),
                    paymentAmount: this.amountFormControl.value,
                }),
                { headers: { "Content-Type": "application/json" } }
            )
                .subscribe({
                    next: (result) => {
                        this.dateFormControl.setValue(new Date());
                        this.amountFormControl.setValue(0.0);
                        this.loadPayments();
                    },
                    error: commonHttpErrorHanlder(this.dialog),
                });
        }
    }

    private loadPayments() {
        this.httpClient.get<GetResidentLogResponse>("http://localhost:8080/neo/residentes/pagos", { params: { licensePlate: this.licensePlate() } })
            .subscribe({
                complete: () => this.isLoading.set(false),
                next: (response) => {
                    this.residentInfo.set({
                        ...response.resident,
                        accumulatedTime: timeMinutesToText(response.resident.accumulatedTime),
                        amountLeft: Math.abs(response.resident.pendingAmount),
                        isPending: response.resident.pendingAmount >= 0,
                    });
                    this.payments.set(response.payments.map(payInfo => ({
                        ...payInfo,
                        paymentDate: new Date(payInfo.paymentDate),
                    })));
                },
                error: (httpResponse: HttpErrorResponse) => {
                    commonHttpErrorHanlder(this.dialog)(httpResponse).afterClosed().subscribe(() => {
                        if (400 === httpResponse.status || 404 === httpResponse.status) {
                            this.router.navigate(["/"]);
                        }
                    });
                },
            });
    }
}
