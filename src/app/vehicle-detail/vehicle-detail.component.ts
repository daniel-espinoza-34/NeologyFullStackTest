import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, inject, input, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { GetResidentResponse, ParkingListResponse, Resident, GetVehicleResponse, type ParkingRecord } from './vehicle-detail.model';
import { timeMinutesToText } from '../../utils/utils';
import { Router, RouterModule } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { alertBuilder, commonHttpErrorHanlder } from '../common/alert/alert.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
    selector: 'app-vehicle-detail',
    imports: [MatButtonModule, MatTableModule, MatProgressSpinner, RouterModule],
    templateUrl: './vehicle-detail.component.html',
    styleUrl: './vehicle-detail.component.css'
})
export class VehicleDetailComponent implements AfterViewInit {
    private httpClient = inject(HttpClient);
    private router = inject(Router);
    readonly licensePlate = input.required<string>();
    readonly vehicleType = signal<string>('');
    readonly displayedColumns = ["startTime", "endTime", "duration"];
    readonly isLoadingRecords = signal(true);
    readonly isLoadingResident = signal(true);
    readonly residentInfo = signal<Resident | null>(null);
    readonly parkingRecords = signal<ParkingRecord[]>([]);
    readonly totalParking = signal<string>("N/A");
    readonly totalExitFare = signal<number>(0);

    constructor(private dialog: MatDialog) { }

    ngAfterViewInit() {
        this.httpClient.get<GetVehicleResponse>(`http://localhost:8080/neo/vehiculos/${this.licensePlate()}`)
            .subscribe({
                next: (response) => {
                    this.vehicleType.set(response.vehicleType.name);
                    if ("residente" === response.vehicleType.vehicleType) {
                        this.httpClient.get<GetResidentResponse>("http://localhost:8080/neo/residentes", { params: { licensePlate: this.licensePlate() } })
                            .subscribe({
                                next: (data) => {
                                    this.residentInfo.set({
                                        ...data,
                                        accumulatedTime: timeMinutesToText(data.accumulatedTime),
                                        amountLeft: Math.abs(data.pendingAmount),
                                        isPending: data.pendingAmount >= 0,
                                    });
                                },
                                error: (httpResponse: HttpErrorResponse) => commonHttpErrorHanlder(this.dialog)(httpResponse),
                                complete: () => this.isLoadingResident.set(false),
                            });
                    } else {
                        this.isLoadingResident.set(false);
                    }
                    this.httpClient.get<ParkingListResponse>("http://localhost:8080/neo/estancias", { params: { licensePlate: this.licensePlate() } })
                        .subscribe({
                            next: (response) => {
                                if (response.records.some((rec) => !!rec.exitFare)) {
                                    this.displayedColumns.push('exitFare');
                                }
                                this.parkingRecords.set(
                                    response.records.map((rec) => ({
                                        ...rec,
                                        startTime: new Date(rec.startTime),
                                        endTime: rec.endTime ? new Date(rec.endTime) : undefined,
                                        duration: timeMinutesToText(rec.duration),
                                    }))
                                );
                                this.totalParking.set(timeMinutesToText(response.totalMinutes));
                                this.totalExitFare.set(response.totalRate);
                            },
                            error: (httpResponse: HttpErrorResponse) => commonHttpErrorHanlder(this.dialog)(httpResponse),
                            complete: () => this.isLoadingRecords.set(false),
                        });
                },
                error: (httpResponse: HttpErrorResponse) => {
                    if (httpResponse.status === 404) {
                        alertBuilder(this.dialog)("Error", `La placa ${this.licensePlate()} no se encuentra registrada`)
                            .afterClosed()
                            .subscribe(() => {
                                this.router.navigate(["/"]);
                            });
                    }
                }
            });
    }
}
