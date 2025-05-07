import { Routes } from '@angular/router';
import { VehicleListComponent } from './vehicle-list/vehicle-list.component';
import { VehicleDetailComponent } from './vehicle-detail/vehicle-detail.component';
import { ResidentReportComponent } from './resident-report/resident-report.component';

export const routes: Routes = [
    { path: "", component: VehicleListComponent, },
    { path: "vehicle-detail/:licensePlate", component: VehicleDetailComponent, },
    { path: "payment-log/:licensePlate", component: ResidentReportComponent, },
];
