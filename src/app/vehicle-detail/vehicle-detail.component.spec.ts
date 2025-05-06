import { TestBed } from '@angular/core/testing';

import { VehicleDetailComponent } from './vehicle-detail.component';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('VehicleDetailComponent', () => {
    let httpTesting: HttpTestingController;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [VehicleDetailComponent, MatButtonModule, MatTableModule, MatProgressSpinner, RouterModule,],
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
        const fixture = TestBed.createComponent(VehicleDetailComponent);
        fixture.componentRef.setInput("licensePlate", "");
        const component = fixture.componentInstance;
        fixture.detectChanges();
        httpTesting.expectOne("http://localhost:8080/neo/vehiculos/", "Buscar vehiculo").flush({
            vehicleType: { name: "" }
        });
        fixture.detectChanges();
        httpTesting.expectOne("http://localhost:8080/neo/estancias?licensePlate=", "Listar estancias").flush({
            records: [],
            totalRate: 0,
            totalMinutes: 0,
        });
        expect(component).toBeTruthy();
    });

    it('should list vehicle type no-residente', () => {
        const fixture = TestBed.createComponent(VehicleDetailComponent);
        const component = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        httpTesting.expectOne("http://localhost:8080/neo/vehiculos/TEST-123", "Buscar vehiculo no-residente").flush({
            vehicleType: { name: "no-residente", vehicleType: "no-residente" }
        });
        httpTesting.expectOne("http://localhost:8080/neo/estancias?licensePlate=TEST-123", "Listar estancias no-residente").flush({
            records: [{
                id: 1,
                licensePlate: "TEST-123",
                startTime: new Date().toISOString(),
                endTime: new Date(new Date().getTime() + (1000 * 60 * 100)).toISOString(),
                duration: 100,
                exitFare: 50,
            },],
            totalRate: 50,
            totalMinutes: 100,
        });
        expect(component.displayedColumns).toContain("exitFare");
        expect(compiled.querySelectorAll("table").length).toBe(1);
    });

    it('should list vehicle type residente', () => {
        const fixture = TestBed.createComponent(VehicleDetailComponent);
        const component = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        httpTesting.expectOne("http://localhost:8080/neo/vehiculos/TEST-123", "Buscar vehiculo residente").flush({
            vehicleType: { name: "residente", vehicleType: "residente" }
        });
        httpTesting.expectOne("http://localhost:8080/neo/residentes?licensePlate=TEST-123", "Buscar residente info").flush({
            licencePlate: "TEST-123",
            accumulatedTime: 100,
            accumulatedRate: 5,
            coveredAmount: 0,
            pendingAmount: 5,
        });
        httpTesting.expectOne("http://localhost:8080/neo/estancias?licensePlate=TEST-123", "Listar estancias residente").flush({
            records: [{
                id: 1,
                licensePlate: "TEST-123",
                startTime: new Date().toISOString(),
                endTime: new Date(new Date().getTime() + (1000 * 60 * 100)).toISOString(),
                duration: 100,
                //exitFare: 50,
            },],
            //totalRate: 50,
            totalMinutes: 100,
        });
        fixture.detectChanges();
        expect(component.displayedColumns).not.toContain("exitFare");
        expect(compiled.querySelectorAll("table").length).toBe(2);
    });

    it('should list vehicle type oficiales', () => {
        const fixture = TestBed.createComponent(VehicleDetailComponent);
        const component = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        httpTesting.expectOne("http://localhost:8080/neo/vehiculos/TEST-123", "Buscar vehiculo oficial").flush({
            vehicleType: { name: "oficiales", vehicleType: "oficial" }
        });
        httpTesting.expectOne("http://localhost:8080/neo/estancias?licensePlate=TEST-123", "Listar estancias oficiales").flush({
            records: [
                {
                    id: 1,
                    licensePlate: "TEST-123",
                    startTime: new Date().toISOString(),
                    endTime: new Date(new Date().getTime() + (1000 * 60 * 100)).toISOString(),
                    duration: 100,
                    //exitFare: 50,
                },
                {
                    id: 2,
                    licensePlate: "TEST-123",
                    startTime: new Date(new Date().getTime() + (1000 * 60 * 120)).toISOString(),
                    //duration: 100,
                    //exitFare: 50,
                },
            ],
            //totalRate: 50,
            totalMinutes: 100,
        });
        fixture.detectChanges();
        expect(component.displayedColumns).not.toContain("exitFare");
        expect(compiled.querySelectorAll("table").length).toBe(1);
        expect(compiled.querySelectorAll("tbody tr").length).toBe(2);
    });
});
