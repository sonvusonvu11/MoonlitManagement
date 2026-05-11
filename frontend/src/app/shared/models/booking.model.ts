export interface Booking {
  bookingID: number;
  roomNumber: number;
  roomTypeName: string;
  guestName: string;
  checkinDate: string;
  checkoutDate: string;
  nights: number;
  totalPrice: number;
}

export interface BookingRequest {
  roomNumber: number;
  checkinDate: string;
  checkoutDate: string;
}
