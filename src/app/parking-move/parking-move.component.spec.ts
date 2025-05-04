import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParkingMoveComponent } from './parking-move.component';

describe('ParkingMoveComponent', () => {
  let component: ParkingMoveComponent;
  let fixture: ComponentFixture<ParkingMoveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParkingMoveComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParkingMoveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
