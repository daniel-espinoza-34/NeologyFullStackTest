import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleListComponent } from './vehicle-list.component';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

describe('VehicleListComponent', () => {
    let httpTesting: HttpTestingController;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [VehicleListComponent, MatTableModule, MatSortModule, MatPaginatorModule, MatButtonModule, MatProgressSpinner, RouterLink],
            providers: [
                { provide: ActivatedRoute, useValue: {} },
                provideHttpClient(),
                provideHttpClientTesting(),
            ]
        }).compileComponents();
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterAll(() => {
        httpTesting.verify();
    });

    it('should create', () => {
        const fixture: ComponentFixture<VehicleListComponent> = TestBed.createComponent(VehicleListComponent);
        const component: VehicleListComponent = fixture.componentInstance;
        fixture.detectChanges();
        httpTesting.expectOne(
            (request) => request.url === "http://localhost:8080/neo/vehiculos", "Listar vehiculos"
        ).flush({ content: [], page: { totalElements: 0, totalPages: 0, } });
        expect(component).toBeTruthy();
    });

    it('should list many results', () => {
        const fixture: ComponentFixture<VehicleListComponent> = TestBed.createComponent(VehicleListComponent);
        const component: VehicleListComponent = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.detectChanges();
        httpTesting.expectOne(
            (request) => request.url === "http://localhost:8080/neo/vehiculos", "Listar varios vehiculos"
        ).flush({
            content: [
                { licensePlate: "TEST-123-O", activeParking: false, vehicleType: { name: 'Oficial', vehicleType: "oficial", }, },
                { licensePlate: "TEST-123-R", activeParking: false, vehicleType: { name: 'Residente', vehicleType: "residente", }, },
                { licensePlate: "TEST-123-N", activeParking: false, vehicleType: { name: 'No-Residente', vehicleType: "no-residente", }, },
                { licensePlate: "TEST-123-A", activeParking: true, vehicleType: { name: 'Oficial', vehicleType: "oficial", }, },
            ], page: { totalElements: 4, totalPages: 1, }
        });
        fixture.detectChanges();
        expect(compiled.querySelectorAll("tbody tr").length).toBe(4);
    });

    it('should list oficial results', () => {
        const fixture: ComponentFixture<VehicleListComponent> = TestBed.createComponent(VehicleListComponent);
        const component: VehicleListComponent = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.detectChanges();
        httpTesting.expectOne(
            (request) => request.url === "http://localhost:8080/neo/vehiculos", "Listar vehiculos oficiales"
        ).flush({
            content: [
                { licensePlate: "TEST-123-O", activeParking: false, vehicleType: { name: 'Oficial', vehicleType: "oficial", }, },
            ], page: { totalElements: 1, totalPages: 1, }
        });
        fixture.detectChanges();
        expect(compiled.querySelectorAll("tbody tr").length).toBe(1);
        expect(compiled.querySelector("tbody tr button")!.textContent).toContain("Registrar Entrada");
        expect(compiled.querySelectorAll("tbody tr a")[0].textContent).toContain("Ver Detalle");
        expect(compiled.querySelectorAll("tbody tr a")[1].getAttribute("disabled")).toBe("true");
    });

    it('should list resident results', () => {
        const fixture: ComponentFixture<VehicleListComponent> = TestBed.createComponent(VehicleListComponent);
        const component: VehicleListComponent = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.detectChanges();
        httpTesting.expectOne(
            (request) => request.url === "http://localhost:8080/neo/vehiculos", "Listar vehiculos residentes"
        ).flush({
            content: [
                { licensePlate: "TEST-123-R", activeParking: false, vehicleType: { name: 'Residente', vehicleType: "residente", }, },
            ], page: { totalElements: 1, totalPages: 1, }
        });
        fixture.detectChanges();
        expect(compiled.querySelectorAll("tbody tr").length).toBe(1);
        expect(compiled.querySelector("tbody tr button")!.textContent).toContain("Registrar Entrada");
        expect(compiled.querySelectorAll("tbody tr a")[0].textContent).toContain("Ver Detalle");
        expect(compiled.querySelectorAll("tbody tr a")[1].getAttribute("disabled")).toBeFalsy();
    });

    it('should list no-resident results', () => {
        const fixture: ComponentFixture<VehicleListComponent> = TestBed.createComponent(VehicleListComponent);
        const component: VehicleListComponent = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.detectChanges();
        httpTesting.expectOne(
            (request) => request.url === "http://localhost:8080/neo/vehiculos", "Listar vehiculos no-residentes"
        ).flush({
            content: [
                { licensePlate: "TEST-123-N", activeParking: false, vehicleType: { name: 'No-Residente', vehicleType: "no-residente", }, },
            ], page: { totalElements: 1, totalPages: 1, }
        });
        fixture.detectChanges();
        expect(compiled.querySelectorAll("tbody tr").length).toBe(1);
        expect(compiled.querySelector("tbody tr button")!.textContent).toContain("Registrar Entrada");
        expect(compiled.querySelectorAll("tbody tr a")[0].textContent).toContain("Ver Detalle");
        expect(compiled.querySelectorAll("tbody tr a")[1].getAttribute("disabled")).toBe("true");
    });

    it('should list active results', () => {
        const fixture: ComponentFixture<VehicleListComponent> = TestBed.createComponent(VehicleListComponent);
        const component: VehicleListComponent = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.detectChanges();
        httpTesting.expectOne(
            (request) => request.url === "http://localhost:8080/neo/vehiculos", "Listar vehiculos activos"
        ).flush({
            content: [
                { licensePlate: "TEST-123-A", activeParking: true, vehicleType: { name: 'Oficial', vehicleType: "oficial", }, },
            ], page: { totalElements: 1, totalPages: 1, }
        });
        fixture.detectChanges();
        expect(compiled.querySelectorAll("tbody tr").length).toBe(1);
        expect(compiled.querySelector("tbody tr button")!.textContent).toContain("Registrar Salida");
        expect(compiled.querySelectorAll("tbody tr a")[0].textContent).toContain("Ver Detalle");
        expect(compiled.querySelectorAll("tbody tr a")[1].getAttribute("disabled")).toBe("true");
    });
});
