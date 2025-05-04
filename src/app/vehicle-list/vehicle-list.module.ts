export interface Vehicle {
    licensePlate: string;
    vehicleType: {
        name: string;
        vehicleType: string;
    };
    activeParking: boolean;
}

export interface GetVehicleListResult {
    content: Vehicle[];
    page: {
        totalElements: number;
        totalPages: number;
    };
}