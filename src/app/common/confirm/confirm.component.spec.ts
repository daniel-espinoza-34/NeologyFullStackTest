import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { confirmBuilder, ConfirmComponent } from './confirm.component';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef, } from '@angular/material/dialog';
import { ConfirmProperties } from './confirm.model';
import { Observable, of, Subject } from 'rxjs';

describe('ConfirmComponent', () => {
    let component: ConfirmComponent;
    let fixture: ComponentFixture<ConfirmComponent>;
    let matDialogRef: MatDialogRef<ConfirmComponent, boolean>;
    const matDialogData: ConfirmProperties = {
        title: "Test Confimr",
        message: ["Test text for Confirm"],
        confirmText: "Confirm",
        cancelText: "Cancel"
    };
    const matDialogSpy = () => {
        const auxObs = new Subject<boolean>();
        return {
            open: jasmine.createSpy('open').and.returnValue({
                afterClosed: () => auxObs
            }),
            close: (val?: boolean) => {
                auxObs.next(!!val);
                auxObs.complete();
            }
        };
    };

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ConfirmComponent, MatDialogModule, MatButtonModule,],
            providers: [
                { provide: MAT_DIALOG_DATA, useValue: matDialogData },
                { provide: MatDialogRef, useFactory: matDialogSpy },
            ],
        }).compileComponents();

        fixture = TestBed.createComponent(ConfirmComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
        matDialogRef = TestBed.inject(MatDialogRef);
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should render dialog', () => {
        const fixture = TestBed.createComponent(ConfirmComponent);
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        expect(compiled.querySelector('h2')?.textContent).toContain(matDialogData.title);
        expect(compiled.querySelector('p')?.textContent).toContain(matDialogData.message);
        expect(compiled.querySelector('button:first-child')?.textContent).toContain(matDialogData.cancelText);
        expect(compiled.querySelector('button:last-child')?.textContent).toContain(matDialogData.confirmText);
    });

    it('should confirm', waitForAsync(() => {
        const fixture = TestBed.createComponent(ConfirmComponent);
        confirmBuilder(matDialogRef as any)(matDialogData).afterClosed().subscribe(confirm => { expect(confirm).toBeTruthy(); });
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        compiled.querySelector('button:last-child')?.dispatchEvent(new Event('click'));
    }));

    it('should cancel', waitForAsync(() => {
        const fixture = TestBed.createComponent(ConfirmComponent);
        confirmBuilder(matDialogRef as any)(matDialogData).afterClosed().subscribe(confirm => { expect(confirm).toBeFalsy(); });
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        compiled.querySelector('button:first-child')?.dispatchEvent(new Event('click'));
    }));

});
