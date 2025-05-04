import { AfterViewInit, Component, inject, signal, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule, MatSort, SortDirection } from '@angular/material/sort';
import { MatTableModule, } from '@angular/material/table'
import { VehicleAddComponent } from '../vehicle-add/vehicle-add.component';
import { ParkingMoveComponent } from '../parking-move/parking-move.component';
import { HttpClient, HttpParams } from '@angular/common/http';
import { merge } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { VehicleDetailComponent } from '../vehicle-detail/vehicle-detail.component';
import { GetVehicleListResult, type Vehicle } from './vehicle-list.module';
import { ResidentReportComponent } from '../resident-report/resident-report.component';
import { commonHttpErrorHanlder } from '../common/alert/alert.component';
import { confirmBuilder } from '../common/confirm/confirm.component';
import { RouterLink } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
    selector: 'app-vehicle-list',
    imports: [MatTableModule, MatSortModule, MatPaginatorModule, MatButtonModule, MatProgressSpinner, RouterLink],
    templateUrl: './vehicle-list.component.html',
    styleUrl: './vehicle-list.component.css'
})
export class VehicleListComponent implements AfterViewInit {
    displayedColumns = ["licensePlate", "vehicleType", "activeParking", "actions"];
    private _httpClient = inject(HttpClient);
    dialog = inject(MatDialog);
    data = signal<Vehicle[]>([]);
    resultsLength = signal(0);
    isLoading = signal(true);

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    ngAfterViewInit() {
        const restService = new VehicleRestService(this._httpClient);
        merge(this.sort.sortChange, this.paginator.page)
            .pipe(
                startWith({}),
                switchMap(() => {
                    this.isLoading.set(true);
                    return restService.getAllVehicles('', this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize)
                }),
            ).subscribe({
                next: (response) => {
                    this.data.set(response.content);
                    this.resultsLength.set(response.page.totalElements);
                },
                error: (httpResponse) => {
                    commonHttpErrorHanlder(this.dialog)(httpResponse);
                    this.data.set([]);
                    this.resultsLength.set(0);
                },
                complete: () => {
                    this.isLoading.set(false);
                }
            });
    }

    handleVehicleAdd(vehicleType: string) {
        this.dialog.open(VehicleAddComponent, {
            data: { vehicleType }, width: '600px'
        }).afterClosed().subscribe({
            next: (res) => {
                if (res) {
                    this.sort.sortChange.emit();
                }
            }
        });
    }

    onParkingMove(licensePlate: string, type: "entrada" | "salida") {
        this.dialog.open(ParkingMoveComponent, {
            data: { licensePlate, type }, width: '600px'
        }).afterClosed().subscribe({
            next: (res) => {
                if (res) {
                    this.sort.sortChange.emit();
                }
            }
        });
    }

    onViewDetail(licensePlate: string, vehicleType: string) {
        this.dialog.open(VehicleDetailComponent, {
            data: { licensePlate, vehicleType }, minWidth: '800px'
        });
    }

    onViewPaymentLog(licensePlate: string) {
        this.dialog.open(ResidentReportComponent, {
            data: { licensePlate }, minWidth: '800px'
        });
    }

    handleTrackRestart() {
        confirmBuilder(this.dialog)({
            title: "¿Seguro que desea continuar?",
            message: [
                "Se eliminara toda la informacion guardada de estancias y pagos",
                "Esta accion es irreversible"
            ],
            confirmText: "Continuar",
            cancelText: "Cancelar",
        }).afterClosed().subscribe({
            next: (accepted) => {
                console.log(accepted);
                if (accepted) {
                    this._httpClient.post(
                        "http://localhost:8080/neo/mes/reiniciar", null,
                        {
                            headers: { "Operation-Auth": "Neo-Approved" }
                        })
                        .subscribe({
                            next: () => this.sort.sortChange.emit(),
                            error: commonHttpErrorHanlder(this.dialog),
                        });
                }
            }
        })
    }
}

class VehicleRestService {
    constructor(private _httpClient: HttpClient) { }
    getAllVehicles(licensePlateFilter: string, sortBy: string, sortDirection: SortDirection, page: number, resultsPerPage: number) {
        let params = new HttpParams({ fromObject: { page, resultsPerPage } });
        if (licensePlateFilter) {
            params = params.append("licensePlateFilter", licensePlateFilter);
        }
        if (sortBy) {
            params = params.append("sortBy", sortBy);
        }
        if (sortDirection) {
            params = params.append("sortDirection", sortDirection);
        }

        return this._httpClient.get<GetVehicleListResult>("http://localhost:8080/neo/vehiculos/", { params });
    }
}
