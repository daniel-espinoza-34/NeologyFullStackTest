import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { ActivatedRoute, RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { MatToolbar } from '@angular/material/toolbar';
import { timeMinutesToText } from '../utils/utils';

describe('AppComponent', () => {
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [AppComponent, MatToolbar, RouterModule],
            providers: [{ provide: ActivatedRoute, useValue: {} }]
        }).compileComponents();
    });

    it('should create the app', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;
        expect(app).toBeTruthy();
    });

    it(`should have the 'NeoParking' title`, () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;
        expect(app.title).toEqual('NeoParking');
    });

    it('should utils timeMinutesToText works', () => {
        expect(timeMinutesToText(0)).toBe("N/A");
        expect(timeMinutesToText(5)).toBe("5 min.");
        expect(timeMinutesToText(60)).toBe("1 hora");
        expect(timeMinutesToText(65)).toBe("1 hora 5 min.");
        expect(timeMinutesToText(120)).toBe("2 horas");
        expect(timeMinutesToText(125)).toBe("2 horas 5 min.");
        expect(timeMinutesToText(60 * 24)).toBe("1 dia");
        expect(timeMinutesToText((60 * 24) + 5)).toBe("1 dia 5 min.");
        expect(timeMinutesToText((60 * 24) + 60)).toBe("1 dia 1 hora");
        expect(timeMinutesToText((60 * 24) + 65)).toBe("1 dia 1 hora 5 min.");
        expect(timeMinutesToText((60 * 24) + 120)).toBe("1 dia 2 horas");
        expect(timeMinutesToText((60 * 24) + 125)).toBe("1 dia 2 horas 5 min.");
        expect(timeMinutesToText(60 * 24 * 2)).toBe("2 dias");
        expect(timeMinutesToText((60 * 24 * 2) + 5)).toBe("2 dias 5 min.");
        expect(timeMinutesToText((60 * 24 * 2) + 60)).toBe("2 dias 1 hora");
        expect(timeMinutesToText((60 * 24 * 2) + 65)).toBe("2 dias 1 hora 5 min.");
        expect(timeMinutesToText((60 * 24 * 2) + 120)).toBe("2 dias 2 horas");
        expect(timeMinutesToText((60 * 24 * 2) + 125)).toBe("2 dias 2 horas 5 min.");
    });
});
