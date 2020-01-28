import { Brand, BrandSymbol } from './Brand';

export type BrandedRecord<K extends Brand<any, any, any>, V> = Record<
  BrandSymbol<K>,
  V
>;
export const getBrandedRecordKey = <T extends Brand<any, any, any>>(
  key: T
): BrandSymbol<T> => {
  return key as any;
};
export const makeEmptyBrandedRecord = <
  T extends BrandedRecord<any, any>
>(): T => {
  return {} as any;
};

export const makeGetBrandedRecordKey = <
  T extends string | number | symbol,
  B extends string | number | symbol
>(
  f: (key1: T) => B
) => <Record extends BrandedRecord<B, any>>(key2: T) => {
  return getBrandedRecordKey<Record>(f(key2) as any);
};

// import { AppError } from "models/Error";
// import { EntityErrorType } from "models/Common";
// import { ICommunication } from "utils/redux/communication";

// declare const teamName: unique symbol;
// type TeamName = Brand<string, 'teamName', typeof teamName>;

// declare const teamId: unique symbol;
// type TeamId = Brand<string, 'teamId', typeof teamId>;

// type G = BrandSymbol<TeamName>;
// const g23: G = teamId;

// type TeamNames = BrandedRecord<TeamName, ICommunication<AppError<EntityErrorType>>>;
// const teamNames: TeamNames = makeEmptyBrandedRecord();
// const teamIds: BrandedRecord<TeamId, ICommunication<AppError<EntityErrorType>>> = teamNames; // compile error

// const key: TeamName = 'dfdf' as any;
// const key2: TeamId = 'dfdf' as any;

// const z = teamNames[getBrandedRecordKey('df')]; // compile error
// const h = teamNames[getBrandedRecordKey(key2)]; // compile error
// const g = teamNames[getBrandedRecordKey(key)];

// const t: TeamNames = {
//   ...teamNames,
//   [getBrandedRecordKey(key)]: ({} as any),
// };

// const hg = teamIds[getBrandedRecordKey(key)]; // compile error
