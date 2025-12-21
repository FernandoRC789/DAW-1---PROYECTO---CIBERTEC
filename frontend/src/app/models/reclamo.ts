export interface Reclamo {
  id?: number;
  asunto: string;
  descripcion: string;
  direccionAfectada: string;
  fotoUrl?: string;
  ciudadanoId: number;
  tipoReclamoId: number;
}
